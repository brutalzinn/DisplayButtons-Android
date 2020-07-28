package net.nickac.buttondeck.utils;

public class Json {

    private int slot;
    private int arraylenght;
    private byte[] internalbtpm;
    private String font;
    private int size;
    private String text;
    private int position;
    private String color;

    public int ArrayLenght() {
        return slot;
    }
    public void ArrayLenght(int state) {
        this.slot = state;
    }
    public byte[] InternalBtpm() {
        return internalbtpm;
    }
    public void InternalBtpm(byte[] state) {
        this.internalbtpm = state;
    }
    public String Text() {
        return text;
    }
    public void Text(String state) {
        this.text = state;
    }
    public String Font() {
        return font;
    }
    public void Font(String state) {
        this.font = state;
    }
    public int Slot() {
        return slot;
    }
    public void Slot(int state) {
        this.slot = state;
    }
    public int Size() {
        return size;
    }
    public void Size(int state) {
        this.size = state;
    }
    public int Position() {
        return position;
    }
    public void Position(int state) {
        this.position = state;
    }

}
