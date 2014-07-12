# h2-tool

A Leiningen plugin to use h2 database.

## Usage
* add configurations to project.clj
like...
```
  :h2-tools {
    web: {
      :allow-others? false ;; -webAllowOthers
      :daemon? true ;; -webDaemon
      :port 8082;; -webPort
      :ssl? false ;; -webSSL
    }
    tcp: {
      :allow-others? false ;; -tcpAllowOthers
      :daemon? true ;; -tcpDaemon
      :port 9092 ;; -tcpPort
      :ssl? false ;; -tcpSSL
      :password "passpass" ;; -tcpPassword
      :shutdown "tcp://localhost" ;; -tcpShutdown
      :shutdown-force? true ;; -tcpShutdownForce
    }
    pg: {
      :allow-others? false ;; -pgAllowOthers
      :daemon? true ;; -pgDaemon
      :port 5435 ;; -pgPort
    }
    ftp: {
      :port 7070 ;; -ftpPort
      :dir "/home/ftp" ;; -ftpDir
      :write "write-user" ;; -ftpWrite
      :write-password "write-p@ss" ;; -ftpWritePassword
      :read "read-user" ;; -ftpRead
      :task? false ;; -ftpTask
    }
    :trace? false ;; -trace
    :if-exists? ;; -IfExists
    :base-dir "/home/h2/databases/test" ;; -baseDir
    :key ["from" "to"] ;; -key
    :properties "~" ;; -properties
  }
```

* Then start server.
```
$ lein h2-tools server
```
