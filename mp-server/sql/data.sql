INSERT INTO `sys_role` VALUES (11, '超级管理员', '所有权限', '1', 'system', '2020-6-10 16:45:34', 'system', '2020-6-10 16:45:34');
INSERT INTO `sys_role` VALUES (21, '普通用户', '普通权限', '2', 'system', '2020-6-10 16:46:54', 'system', '2020-6-10 16:46:54');

INSERT INTO `sys_user` VALUES (51, 1, 'admin', '$2a$10$iEpGPe7J2Zj23juaZVCi7.XR/GlzuK3IDkzd8SGPSkPn64xUNvOsG', '管理员', '0001', '1', 'admin@qq.com', NULL, '13811112222', '1', NULL, '1', 'system', '2020-6-10 16:34:38', 'system', '2020-6-10 16:34:47', NULL, '0');

INSERT INTO `sys_user_role` VALUES (1, 51, 11);
