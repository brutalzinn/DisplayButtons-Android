package net.robertocpaes.displaybuttons.utils;

public class Json {



public class JsonTester {
    public  int slot;
    public  int arraylenght;
    public  byte[] internalbtpm;
    public  String font;
    public  int size;
    public  String text;
    public  int position;
    public  String color;

}
    private final int slot;
    private final int arraylenght;
    private final byte[] internalbtpm;
    private final String font;
    private final int size;
    private final String text;
    private final int position;
    private final String color;

    public int ArrayLenght() {
        return arraylenght;
    }
    public Json(int _slot, int _arraylenght, String _font, byte[] _internalbtmp, int _size, String _text,int _position,String _color){
        slot = _slot;
        arraylenght = _arraylenght;
        font = _font;
        size = _size;
        text = _text;
        position =_position;
        color = _color;
        internalbtpm = _internalbtmp;


    }
    public byte[] InternalBtpm() {
        return internalbtpm;
    }

    public String Text() {
        return text;
    }

    public String Font() {
        return font;
    }

    public int Slot() {
        return slot;
    }
    public int Size() {
        return size;
    }

    public int Position() {
        return position;
    }


}
