package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.barzahlung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;
import de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.ObservableSubwerkzeug;

/**
 * TODO für Blatt 8: Löschen
 * 
 * Das Barzahlungswerkzeug behandelt die Kasseneingabe. Es ermöglicht die
 * Eingabe eines gezahlten Betrags und ermittelt automatisch den Restbetrag zur
 * Unterstützung des Verkaufs.
 * 
 * Wurde die Bezahlung erfolgreich abgeschlossen, kann dieser Status abgefragt
 * werden. Bei einer erneuten Durchführung einer Bezahlung wird dieser
 * Werkzeugstatus zu Beginn wieder auf nicht erfolgreich (false) gesetzt.
 * 
 * Das Beenden des Bezahlvorgangs (OK oder Abbrechen) führt zu einer
 * Benachrichtigung aller Beobachter, die danach den Status abfragen können.
 * 
 * Das Werkzeug arbeitet mit einem modalen Dialog, d. h. der Programmfluss
 * bleibt in diesem Werkzeug und wird erst nach dem Beenden an den Aufrufer
 * zurückgegeben.
 * 
 * @author SE2-Team
 * @version SoSe 2015
 */
public class BarzahlungsWerkzeug extends ObservableSubwerkzeug
{

    private BarzahlungsWerkzeugUI _ui;
    private Geldbetrag _preis;
    private boolean _barzahlungErfolgreich;
    private boolean _ausreichenderGeldbetrag;

    /**
     * Initialisiert das Werkzeug. Die Aktivierung erfolgt über eine sparate
     * Methode.
     * 
     */
    public BarzahlungsWerkzeug()
    {
        _ui = new BarzahlungsWerkzeugUI();
        registriereUIAktionen();
    }

    /**
     * Startet den Barzahlungsvorgang. Die UI wird angezeigt. Der Programmfluss
     * kehrt erst nach dem Beenden des Bezahlvorgangs an den Aufrufer zurück.
     * 
     * @param preis der einzunehmende Gelbetrag
     */
    //TODO Blatt 9: Führe GELDBETRAG ein
    public void fuehreBarzahlungDurch(Geldbetrag preis)
    {
        _preis = preis;
        _ausreichenderGeldbetrag = false;
        _barzahlungErfolgreich = false;
        setzeUIAnfangsstatus();
        _ui.zeigeAn();
    }

    /**
     * @return true, wenn die Barzahlung erfolgreich durchgeführt wurde, sonst
     *         false.
     */
    public boolean barzahlungErfolgreich()
    {
        return _barzahlungErfolgreich;
    }

    /**
     * Registiert alle Listener an den UI-Widgets
     */
    private void registriereUIAktionen()
    {
        registriereAbbrechenAktionen();
        registriereOKAktion();
        registriereGeyahltTextfieldEingabeAktion();
    }

    /**
     * Registriert einen Listener, der auf den Abbrechen-Button reagiert
     */
    private void registriereAbbrechenAktionen()
    {
        _ui.getAbbrechenButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                bezahlenNichtErfolgreich();
            }

        });
        _ui.getDialog().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                bezahlenNichtErfolgreich();
            }
        });
    }

    /**
     * Registriert einen Listener, der auf das Drücken des OK-Buttons alias
     * "Verkaufen" reagiert.
     */
    private void registriereOKAktion()
    {
        _ui.getGeldErhaltenButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                bezahlenErfolgreich();
            }
        });
    }

    /**
     * Registriert einen Listener, der auf Änderungen im Gegeben-Textfeld
     * reagiert.
     */
    private void registriereGeyahltTextfieldEingabeAktion()
    {
        _ui.getGezahltTextfield().addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                switch (e.getKeyCode())
                {
                case KeyEvent.VK_ESCAPE:
                    bezahlenNichtErfolgreich();
                    break;
                case KeyEvent.VK_ENTER:
                    if (_ausreichenderGeldbetrag)
                    {
                        bezahlenErfolgreich();
                    }
                    break;
                default:
                    reagiereAufEingabeText(_ui.getGezahltTextfield().getText());
                }
            }
        });
    }

    /**
     * Setzt einen neuen Status für das Werkzeug auf Basis der gesamten Eingabe
     * und beendet das Bezahlen erfoglreich, sollte der Preis gedeckt und die
     * Entertaste gedrückt worden sein. Die Esc-Taste beendet den Bezahlvorgang
     * erfolglos.
     * 
     * @param eingabePreis der bisher eingegebene Preis
     */
    private void reagiereAufEingabeText(String eingabePreis)
    {
        if (eingabePreis.isEmpty())
        {
            eingabePreis = "0";
        }
        try
        {
            //TODO Falls Eingabe auch als Geldbetrag soll, dann hier ändern
            int eingabeBetrag = Integer.parseInt(eingabePreis);
            int eurocentpreis = (_preis.getEuroAnteil() * 100) + _preis.getCentAnteil();
            
           if(!Pattern.matches( "^\\d{0,6}$", eingabePreis))
           {
           _ui.getRestbetragTextfield().setText("Betrag zu groß!");
           _ausreichenderGeldbetrag = false;
           }
           else
           {
            _ausreichenderGeldbetrag = (eingabeBetrag >= eurocentpreis);
            int differenz = Math.abs(eingabeBetrag - eurocentpreis);
            zeigeRestbetrag(Geldbetrag.erzeugeGeldbetrag(differenz));
           }
        }
        catch (NumberFormatException ignore)
        {
            _ausreichenderGeldbetrag = false;
            zeigeFehlertext();
        }
        zeigeAusreichenderGeldbetragStatus();
    }

    /**
     * Beendet den Bezahlvorgang mit Erfolg.
     */
    private void bezahlenErfolgreich()
    {
        _barzahlungErfolgreich = true;
        _ui.verberge();
    }

    /**
     * Bricht den Bezahlvorgang ohne Erfolg ab.
     */
    private void bezahlenNichtErfolgreich()
    {
        _barzahlungErfolgreich = false;
        _ui.verberge();
    }

    /**
     * Setzt die UI in einen sinnvollen Anfangszustand.
     */
    private void setzeUIAnfangsstatus()
    {
        zeigePreis();
        loescheGezahltenBetrag();
        zeigeRestbetrag(_preis);
        zeigeAusreichenderGeldbetragStatus();
    }

    /**
     * Löscht den gezahlten Betrag aus der UI.
     */
    private void loescheGezahltenBetrag()
    {
        _ui.getGezahltTextfield().setText("");
    }

    /**
     * Setzt die Statusanzeige der Gegeben- und Rückgabe-Textfelder abhängig
     * davon, ob ein ausreichender Geldbetrag gegeben wurde.
     * 
     */
    private void zeigeAusreichenderGeldbetragStatus()
    {
        _ui.getGeldErhaltenButton().setEnabled(_ausreichenderGeldbetrag);
        _ui.markiereGezahltTextfield(_ausreichenderGeldbetrag);
        _ui.markiereRestbetragTextfield(_ausreichenderGeldbetrag);
    }

    /**
     * Setzt die Fehlerstatusanzeige der Gegeben- und Rückgabe-Textfelder.
     * 
     * @param fehler true, wenn die Felder als fehlerhaft markiert werden
     *            sollen, sonst false.
     */
    private void zeigeFehlertext()
    {
        _ui.getRestbetragTextfield().setText(" Err ");
    }

    /**
     * Setzt eine übergebene Differenz im Restbetrag-Textfeld
     * 
     * @param differenz ein eingegebener Betrag
     */
    private void zeigeRestbetrag(Geldbetrag differenz)
    {
        _ui.getRestbetragTextfield().setText(differenz.getFormatiertenString() + " Euro");
    }

    /**
     * Setzt den Preis in der UI.
     */
    private void zeigePreis()
    {
        _ui.getPreisTextfield().setText(_preis + " Euro");
    }
}
