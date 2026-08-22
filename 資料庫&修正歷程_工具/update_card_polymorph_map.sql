/*
 * 880 變身卡片資料驅動設定。
 * 執行時機：關閉伺服器後，與新版 Server_Game.jar 一起套用。
 * item_id 必須先存在於 etcitem，且已登錄於 道具合成系統。
 */
CREATE TABLE IF NOT EXISTS `card_polymorph_map` (
  `item_id` INT(10) UNSIGNED NOT NULL COMMENT '背包變身卡物品 ID',
  `poly_id` INT(11) NOT NULL COMMENT '已人工確認的 polymorphs.polyid',
  `duration_seconds` INT(10) UNSIGNED NOT NULL DEFAULT 1800 COMMENT '變身秒數（統一 30 分鐘）',
  `enabled` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '1=可由圖鑑使用',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`),
  KEY `idx_card_polymorph_map_poly_id` (`poly_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='變身卡片與外觀代碼對照';

/* 第一張已驗證測試卡：變身卡：狼人（700502） -> 狼人外觀（11340）。 */
INSERT INTO `card_polymorph_map` (`item_id`, `poly_id`, `duration_seconds`, `enabled`)
VALUES (700502, 11340, 1800, 1)
ON DUPLICATE KEY UPDATE
  `poly_id`=VALUES(`poly_id`),
  `duration_seconds`=VALUES(`duration_seconds`),
  `enabled`=VALUES(`enabled`);
