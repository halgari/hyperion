(ns hyperion.postgres-spec
  (:use
    [speclj.core]
    [hyperion.sql.spec-helper]
    [hyperion.dev.spec :only [it-behaves-like-a-datastore]]
    [hyperion.core :only [*ds*]]
    [hyperion.sql.jdbc :only [execute-mutation]]
    [hyperion.sql.query]
    [hyperion.postgres :only [new-postgres-datastore]]))

(def create-table-query
  "CREATE TABLE %s (
    id SERIAL PRIMARY KEY,
    name VARCHAR(35),
    first_name VARCHAR(35),
    inti INTEGER,
    data VARCHAR(32)
  )")

(defn create-table [table-name]
  (execute-mutation
    (make-query (format create-table-query table-name))))

(describe "Postgres Datastore"
  (with-connection-and-rollback "jdbc:postgresql://localhost:5432/hyperion")

  (around [it]
    (create-table "testing")
    (create-table "other_testing")
    (binding [*ds* (new-postgres-datastore)]
      (it)))

  (it-behaves-like-a-datastore))
