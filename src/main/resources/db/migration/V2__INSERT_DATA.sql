INSERT INTO tool (title, link, description)
VALUES
    ('Notion', 'https://notion.so', 'All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.'),
    ('json-server', 'https://github.com/typicode/json-server', 'Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.'),
    ('fastify', 'https://www.fastify.io/', 'Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.');

INSERT INTO tag(tool_id, tags)
VALUES
    (1, 'organization'),
    (1, 'planning'),
    (1, 'collaboration'),
    (1, 'writing'),
    (1, 'calendar'),
    (2, 'api'),
    (2, 'json'),
    (2, 'schema'),
    (2, 'node'),
    (2, 'github'),
    (2, 'rest'),
    (3, 'web'),
    (3, 'framework'),
    (3, 'node'),
    (3, 'http2'),
    (3, 'https'),
    (3, 'localhost');

