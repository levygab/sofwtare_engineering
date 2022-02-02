package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;


public class TableType extends Type {

    private Type subType;

    // public SymbolTable.Symbol getSubType() {
    //     return this.subType;
    // }

    public TableType(SymbolTable.Symbol name) {
        super(name);
        this.subType = null;
        
    }

    public TableType(SymbolTable.Symbol name, Type subType) {
        super(name);
        this.subType = subType;
    }

    public Type getSubType() {
        return this.subType;
    }

    public void setSubType(Type subType) {
        this.subType = subType;
    }

    @Override
    public boolean isTable() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        boolean c1 = otherType.isTable();
        if (!c1) {
            return false;
        }
        boolean c2 = (((TableType) otherType).getSubType() == this.subType);
        return c2;
    }
    
}
