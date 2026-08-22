/* 880 變身卡圖鑑：永久收藏紀錄與 GM 測試入口 */
SET NAMES utf8;
CREATE TABLE IF NOT EXISTS `card_collection` (
  `char_id` INT(11) NOT NULL COMMENT '角色物件 ID',
  `card_id` INT(11) NOT NULL COMMENT '變身卡 item_id',
  `count` INT(11) NOT NULL DEFAULT 1 COMMENT '已取得（保留欄位供日後合成使用）',
  `first_obtained_at` DATETIME NULL DEFAULT NULL COMMENT '首次取得時間',
  PRIMARY KEY (`char_id`, `card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT IGNORE INTO `commands` (`name`, `access_level`, `class_name`, `note`, `system`)
VALUES ('圖鑑', 100, 'L1CardCollection', '開啟變身卡圖鑑（測試入口）', 1);
