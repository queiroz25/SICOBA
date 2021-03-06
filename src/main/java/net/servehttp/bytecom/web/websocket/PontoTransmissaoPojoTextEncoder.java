package net.servehttp.bytecom.web.websocket;

import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import net.servehttp.bytecom.persistence.entity.pingtest.PontoTransmissaoPojo;

import com.google.gson.Gson;

public class PontoTransmissaoPojoTextEncoder implements Encoder.Text<List<PontoTransmissaoPojo>> {

  private Gson gson = new Gson();

  @Override
  public void init(EndpointConfig config) {}

  @Override
  public void destroy() {}

  @Override
  public String encode(List<PontoTransmissaoPojo> pontos) throws EncodeException {
    return gson.toJson(pontos);
  }
}
