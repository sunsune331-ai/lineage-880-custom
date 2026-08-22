/* 偉翔雙刀（objid: 15597482）變身卡圖鑑全數點亮 */
SET NAMES utf8;

INSERT IGNORE INTO `card_collection` (`char_id`, `card_id`, `count`, `first_obtained_at`)
SELECT 15597482, `item_id`, 1, NOW()
FROM `道具合成系統`;
