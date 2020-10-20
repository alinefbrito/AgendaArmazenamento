package com.example.agendasqlite;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Contato implements Serializable {

    private int _id;
    private String _nome;
    private String _num_tel;
    private String _email;
    private String _logradouro;
    private String _cidade;

    public Contato(){   }
    public Contato(int id, String nome, String num_tel){
        this.set_id(id);
        this.set_nome(nome);
        this.set_num_tel(num_tel);
    }

    public Contato(String nome, String num_tel){
        this.set_nome(nome);
        this.set_num_tel(num_tel);
    }

    public Contato(String _nome, String _num_tel, String _email, String _logradouro, String _cidade) {
        this._nome = _nome;
        this._num_tel = _num_tel;
        this._email = _email;
        this._logradouro = _logradouro;
        this._cidade = _cidade;
    }

    public Contato(int _id, String _nome, String _num_tel, String _email, String _logradouro, String _cidade) {
        this._id = _id;
        this._nome = _nome;
        this._num_tel = _num_tel;
        this._email = _email;
        this._logradouro = _logradouro;
        this._cidade = _cidade;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nome() {
        return _nome;
    }

    public void set_nome(String _nome) {
        this._nome = _nome;
    }

    public String get_num_tel() {
        return _num_tel;
    }

    public void set_num_tel(String _num_tel) {
        this._num_tel = _num_tel;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_logradouro() {
        return _logradouro;
    }

    public void set_logradouro(String _logradouro) {
        this._logradouro = _logradouro;
    }

    public String get_cidade() {
        return _cidade;
    }

    public void set_cidade(String _cidade) {
        this._cidade = _cidade;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Id", this._id);
            obj.put("Nome", this._nome);
            obj.put("Tel", this._num_tel);
            obj.put("Email", this._email);
            obj.put("Endereco", this._logradouro);
            obj.put("Cidade", this._cidade);
        } catch (JSONException e) {
            //trace("DefaultListItem.toString JSONException: "+e.getMessage());
        }
        return obj;
    }
}
