import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import pos.machine.Item;
import pos.machine.ItemsLoader;

public class Main {

  public static void main(String[] args) {
    String[] barCodes = {
        "ITEM000000", "ITEM000000", "ITEM000000", "ITEM000000",
        "ITEM000001", "ITEM000001",
        "ITEM000004", "ITEM000004", "ITEM000004"
    };
    System.out.println(createReceiptByBarCode(barCodes));
  }

  public static String createReceiptByBarCode(String[] barCode) {
    HashMap<String, Integer> hashMap = findAllResult(barCode);
    List<String> list = sortByCount(hashMap);
    return printReceipt(list, hashMap);
  }


  private static String printReceipt(List<String> sortedBarcodes, HashMap<String, Integer> countMap) {
    StringBuilder receipt = new StringBuilder();

    receipt.append("<store earning no money>Receipt\n");

    int total = 0;

    for (String barcode : sortedBarcodes) {
      int quantity = countMap.get(barcode);
      int price = findPriceByBarCode(barcode);
      if (price == -1) {
        return null;
      }
      String name = findItemNameByBarcode(barcode);
      int subtotal = price * quantity;
      total += subtotal;
      receipt.append(String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)\n",
          name, quantity, price, subtotal));

    }

    receipt.append("----------------------\n");
    receipt.append(String.format("Total: %d (yuan)\n", total));
    receipt.append("**********************");

    return receipt.toString();
  }

  private static List<String> sortByCount(HashMap<String, Integer> hashMap) {
    List<String> barcodes = new ArrayList<>(hashMap.keySet());

    Collections.sort(barcodes, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return hashMap.get(o2).compareTo(hashMap.get(o1));
      }
    });

    return barcodes;
  }

  private static HashMap<String, Integer> findAllResult(String[] barCode) {
    if (barCode == null || barCode.length == 0) {
      return null;
    }

    HashMap<String, Integer> countMap = new HashMap<>();

    for (String barcode : barCode) {
      countMap.put(barcode, countMap.getOrDefault(barcode, 0) + 1);
    }

    return countMap;
  }


  private static int findPriceByBarCode(String barcode) {
    List<Item> items = ItemsLoader.loadAllItems();
    for (Item item : items) {
      if (item.getBarcode().equals(barcode)) {
        return item.getPrice();
      }
    }
    return -1;
  }

  private static String findItemNameByBarcode(String barcode) {
    List<Item> items = ItemsLoader.loadAllItems();
    for (Item item : items) {
      if (item.getBarcode().equals(barcode)) {
        return item.getName();
      }
    }
    return null;
  }
}