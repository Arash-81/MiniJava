package Compiler;

import java.util.*;


public class SymbolTable {
    HashMap<String, String> items;
    String name;
    String type;
    SymbolTable parent;
    ArrayList<SymbolTable> children;
    private final int scope;
    private final int depth;

    public SymbolTable(SymbolTable parent, String name, String type, int scope, int depth) {
        children = new ArrayList<>();
        items = new HashMap<>();
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.depth = depth;
        if (parent != null)
            parent.children.add(this);
    }

    private String getIndent() {
        return "    ".repeat(Math.max(0, this.depth));
    }

    @Override
    public String toString() {
        return getIndent() + "------------- " + name + " : " + scope + " -------------\n" +
                printItems() +
                getIndent() + "-----------------------------------------\n";
    }

    public void insert(String idefName, String attributes) {
        this.items.put(idefName, attributes);
    }

    public String lookup(String lookup) {
        return this.items.get(lookup);
    }

    public String printItems() {
        StringBuilder itemsStr = new StringBuilder();

        for (Map.Entry<String, String> entry : items.entrySet())
            itemsStr.append(getIndent()).append("Key = ").append(entry.getKey()).append(" | Value = ").append(entry.getValue()).append("\n");

        return itemsStr.toString();
    }

    public void print() {
        System.out.println(this);
        for (SymbolTable child : children) child.print();
    }
}
