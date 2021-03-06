package net.servehttp.bytecom.web.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.servehttp.bytecom.business.AccountBussiness;
import net.servehttp.bytecom.ejb.MailEJB;
import net.servehttp.bytecom.persistence.entity.security.UserAccount;
import net.servehttp.bytecom.util.NetworkUtil;
import net.servehttp.bytecom.util.WebUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * 
 * @author Clairton Luz - clairton.c.l@gmail.com
 *
 */
@Named
@RequestScoped
public class SecurityController implements Serializable {

  private static final long serialVersionUID = -4657746545855537894L;
  private static final Logger LOGGER = Logger.getLogger(SecurityController.class.getSimpleName());
  private static final String destinatario = "clairton.c.l@gmail.com";
  private static final String HOME_URL = "index.xhtml";

  private String username;
  private String password;
  private Subject currentUser = SecurityUtils.getSubject();

  @Inject
  private AccountBussiness accountBussiness;
  @Inject
  private WebUtil webUtil;
  @EJB
  private MailEJB mail;
  @Inject
  private UserAccount userAccount;
  private String error;

  @PostConstruct
  public void carregar() {
    if (currentUser.isAuthenticated()) {
      LOGGER.info("[" + new Date() + "] - " + "USUÁRIO JÁ ESTA LOGADO");
      webUtil.redirect(HOME_URL);
    }
  }

  public void authenticate() {
    error = null;
    if (!currentUser.isAuthenticated()) {
      try {
        currentUser.login(new UsernamePasswordToken(username, password));
        userAccount = accountBussiness.buscarUsuarioPorUsername(username);
        accountBussiness.criarImagemNaSessao(userAccount);
        currentUser.getSession().setAttribute("currentUser", userAccount);
        webUtil.redirect(HOME_URL);
      } catch (AuthenticationException e) {
        error = "CREDENCIAIS INVÁLIDAS";
        LOGGER.info("[" + new Date() + "] - " + "[" + username + "] - " + "ACESSO NEGADO");
        sendAlert();
      }
    }
  }

  public void logout() {
    SecurityUtils.getSubject().logout();
    webUtil.redirect(HOME_URL);
  }

  private void sendAlert() {
    final String assunto = "Tentativa de acesso";
    final String ipQueTentouAcessar = NetworkUtil.INSTANCE.getIp();
    final String mensagem =
        "O seguinte ip tentou acessar o sistema: " + ipQueTentouAcessar
            + " com o seguite usuario: " + username;

    mail.send(destinatario, assunto, mensagem);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

}
