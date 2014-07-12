(ns leiningen.h2-tools
  (:require [clojure.pprint :as pprint]
            [clojure.string :as string]))

(defn -web-config [{web-config :web}]
  (if (nil? web-config) nil
    (flatten (filter #(not (nil? %1))
                     ["-web"
                      (if (true? (:allow-others? web-config)) "-webAllowOthers" nil)
                      (if (false? (:daemon? web-config)) nil "-webDaemon")
                      (if (number? (:port web-config)) ["-webPort" (str (:port web-config))] nil)
                      (if (true? (:ssl? web-config)) "-webSSL" nil)]))))

(defn -tcp-config [{tcp-config :tcp}]
  (if (nil? tcp-config) nil
    (flatten (filter #(not (nil? %1))
                     ["-tcp"
                      (if (true? (:allow-others? tcp-config)) "-tcpAllowOthers" nil)
                      (if (false? (:daemon? tcp-config)) nil "-tcpDaemon")
                      (if (true? (:ssl? tcp-config)) "-tcpSSL" nil)
                      (if (number? (:port tcp-config)) ["-tcpPort" (str (:port tcp-config))] nil)
                      (if (:password tcp-config) ["-tcpPassword" (:password tcp-config)] nil)
                      (if (:shutdown tcp-config) ["-tcpShutdown" (:shutdown tcp-config)] nil)
                      (if (true? (:shutdown-force? tcp-config)) "-tcpShutdownForce" nil)]))))

(defn -pg-config [{pg-config :pg}]
  (if (nil? pg-config) nil
    (flatten (filter #(not (nil? %1))
                     ["-pg"
                      (if (true? (:allow-others? pg-config)) "-pgAllowOthers" nil)
                      (if (false? (:daemon? pg-config)) nil "-pgDaemon")
                      (if (number? (:port pg-config)) ["-pgPort" (str (:port pg-config))] nil)]))))

(defn -ftp-config [{ftp-config :ftp}]
  (if (nil? ftp-config) nil
    (flatten (filter #(not (nil? %1))
                     ["-ftp"
                      (if (number? (:port ftp-config)) ["-ftpPort" (str (:port ftp-config))] nil)
                      (if (:dir ftp-config) ["-ftpDir" (:dir ftp-config)] nil)
                      (if (:read ftp-config) ["-ftpRead" (:read ftp-config)] nil)
                      (if (:write ftp-config) ["-ftpWrite" (:write ftp-config)] nil)
                      (if (:write-password ftp-config) ["-ftpWritePassword" (:write-password ftp-config)] nil)
                      (if (true? (:task? ftp-config)) "-ftpTask" nil)]))))

(defn -config [config]
  (if (nil? config) nil
    (flatten (filter #(not (nil? %1))
                      [(if (true? (:trace? config)) "-trace" nil)
                                  (if (true? (:if-exists? config)) "-ifExists" nil)
                       (if (:base-dir config) ["-baseDir" (:base-dir config)] nil)
                       (if (and (:key config) (= 2 (count (:key config)))) ["-key" (:key config)] nil)
                       (if (:properties config) ["-properties" (:properties config)] nil)]))))

(defn -launch-h2-server [{config :h2-tools}]
  (let [args (concat (-web-config config)
                     (-tcp-config config)
                     (-pg-config config)
                     (-ftp-config config)
                     (-config config))
        server (org.h2.tools.Server.)]
    (println (:base-dir config))
    (pprint/pprint (str "Launching Server. args=" (string/join "," args)))
    (.runTool server (into-array String args))
    (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] (println "Shutting down..."))))
    (while true)))

(defn h2-tools
  "H2 database tools."
  [project & args]
  (let [[command & args] args]
    (condp = command
      "server" (-launch-h2-server project)
      (if command (pprint/pprint (format "Unexpected command %s." command))
        (pprint/pprint "Command not specified.")))))
