package ru.galeev.currencyRate.services.requester;

public interface Requester {
    String getRatesAsXml(String url);
}
