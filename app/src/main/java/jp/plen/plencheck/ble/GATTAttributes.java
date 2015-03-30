package jp.plen.plencheck.ble;

import java.util.HashMap;

/**
 * Created by yuki on 3/30/15.
 */
public class GATTAttributes {
    private static HashMap<String, String> attributes = new HashMap<String, String>();
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String BLE_SHIELD_TX = "F90E9CFE-7E05-44A5-9D75-F13644D6F645";
    public static String BLE_SHIELD_RX = "2ED17A59-FC21-488E-9204-503EB78158D7";
    public static String BLE_SHIELD_SERVICE = "E1F40469-CFE1-43C1-838D-DDBC9DAFDDE6";

    static {
        // RBL Services.
        attributes.put(BLE_SHIELD_SERVICE, "BLE Shield Service");
        // RBL Characteristics.
        attributes.put(BLE_SHIELD_TX, "BLE Shield TX");
        attributes.put(BLE_SHIELD_RX, "BLE Shield RX");
    }
}
