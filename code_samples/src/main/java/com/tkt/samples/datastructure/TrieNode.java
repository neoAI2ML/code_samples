package com.tkt.samples.datastructure;
import java.util.HashMap;

public class TrieNode<T> {
    private HashMap<Character, TrieNode<T>> children;
    private String text;
    private boolean isWord;
    private T dataObject;

    public TrieNode() {
        children = new HashMap<Character, TrieNode<T>>();
        text = "";
        isWord = false;
    }

    public TrieNode(String text) {
        this();
        this.text = text;
    }
    
    public TrieNode(String text, T dataObject) {
        this();
        this.text = text;
        this.dataObject = dataObject;
    }

    public HashMap<Character, TrieNode<T>> getChildren() {
        return children;
    }

    public String getText() {
        return text;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setIsWord(boolean word) {
        isWord = word;
    }

    @Override
    public String toString() {
        return text;
    }

	public T getDataObject() {
		return dataObject;
	}

	public void setDataObject(T dataObject) {
		this.dataObject = dataObject;
	}
}
