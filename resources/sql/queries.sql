-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name create-repo! :! :n
-- :doc creates a new repo record
INSERT INTO repos
(repo_owner, repo_name, stargazers_count, watchers_count, forks_count, open_issues_count)
VALUES (:repo_owner, :repo_name, :stargazers_count, :watchers_count, :forks_count, :open_issues_count)

-- :name list-owners :? :*
-- :doc list all repo owners stored
SELECT DISTINCT repo_owner FROM repos

-- :name get-repos :? :*
-- :doc retrieve repos for given repo_owner
SELECT * FROM repos
WHERE repo_owner = :repo_owner
