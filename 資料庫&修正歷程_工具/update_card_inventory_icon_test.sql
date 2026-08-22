/*
 * 變身卡背包圖示：第一筆測試
 *
 * #11390 是角色動畫（狼人）SPR，不能當背包圖示使用。
 * #11924 是客戶端既有的單張靜態 transformation paper 圖示。
 * 本檔只修改狼人卡（700502），以確認背包顯示正常。
 *
 * 執行前請關閉 880 伺服器；執行後重新啟動伺服器與客戶端。
 */

UPDATE `etcitem`
SET `invgfx` = 11924
WHERE `item_id` = 700502
  AND `invgfx` = 11390;

SELECT `item_id`, `name`, `invgfx`
FROM `etcitem`
WHERE `item_id` = 700502;
