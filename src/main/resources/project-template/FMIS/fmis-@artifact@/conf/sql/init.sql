-- 路由
INSERT INTO `gameserv_system`.`sys_route_conf`( `route_name`, `route_id`, `predicates`, `filters`, `uri`, `order`, `create_time`, `update_time`, `del_flag`)
VALUES ('@desc@', 'fmis-{artifact}-application', '[{\"args\": {\"_genkey_0\": \"/@artifact4hump@Front/**\"}, \"name\": \"Path\"}]', '[]', 'lb://fmis-{artifact}-application', 0, now(), now(), '0');

-- success
