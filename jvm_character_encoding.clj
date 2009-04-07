(import
  '(java.io PrintWriter PrintStream FileInputStream)
  '(java.nio CharBuffer ByteBuffer)
  '(java.nio.charset Charset CharsetDecoder CharsetEncoder)
  '(org.xml.sax InputSource)))

(printf "Default Charset: %s\n" (Charset/defaultCharset))

(def c-roman "MacRoman")
(def c-latin "ISO-8859-1")
(def c-utf8  "UTF-8")

(defmacro with-out-encoded [encoding & body]
  `(binding [*out* (PrintWriter. (PrintStream. System/out true ~encoding) true)]
     ~@body
     (flush)))

(defn printries [s]
  (let [f println]
    (f s)
    (with-out-encoded c-roman (f s))
    (with-out-encoded c-latin (f s))
    (with-out-encoded c-utf8  (f s))))

(printries "québécois français")

(printries
  (let [x (InputSource. (FileInputStream. "jvm_character_encoding.xml"))]
    (.setEncoding x c-latin)
    (:content (clojure.xml/parse x))))

;french.xml, to be saved with proper encoding:
;
; <?xml version="1.0" encoding="ISO-8859-1"?>
; <root>Québécois français</root>
