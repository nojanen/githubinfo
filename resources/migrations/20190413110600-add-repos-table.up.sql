CREATE TABLE repos
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
 repo_owner VARCHAR(30),
 repo_name VARCHAR(50),
 stargazers_count INTEGER,
 watchers_count INTEGER,
 forks_count INTEGER,
 open_issues_count INTEGER);
