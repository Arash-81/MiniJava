package Compiler;
import java.util.*;


public class SymbolTable {
    HashMap<String, String> items;
    String name;
    boolean isClass;
    String return_type;
    private int scopeNumber;

    SymbolTable parent;
    ArrayList<SymbolTable> child;

    boolean check_error;


    public SymbolTable(SymbolTable parent, String name, int scopeNumber, boolean check_error, boolean isClass, String return_type) {
        child = new ArrayList<>();
        items = new HashMap<>();
        this.parent = parent;
        this.name = name;
        this.scopeNumber = scopeNumber;
        if(parent != null){
            parent.child.add(this);
        }
        this.check_error = check_error;
        this.isClass = isClass;
        this.return_type = return_type;
    }

    @Override
    public String toString() {
        return "------------- " + name + " : " + scopeNumber + " -------------\n" +
                printItems() +
                "-----------------------------------------\n";
    }

    public String printItems() {
        String itemsStr = "";
        for (Map.Entry<String, String> entry : items.entrySet()) {
            itemsStr += "Key = " + entry.getKey() + " | Value = " + entry.getValue()
                    + "\n";
        }
        return itemsStr;
    }

    public void print(){
        System.out.println(this.toString());
        for(int i = 0; i< child.size(); i++){
            child.get(i).print();
        }
    }
}
