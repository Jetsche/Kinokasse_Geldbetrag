package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

public final class Geldbetrag
{

    private final int _euroAnteil;
    private final int _centAnteil;

    /**
     * Wählt einen Geldbetrag aus.
     * 
     * @param eurocent Der Betrag in ganzen Euro-Cent
     * 
     * @require eurocent >= 0;
     */
    private Geldbetrag(int eurocent)
    {
        //assert eurocent >= 0 : "Vorbedingung verletzt: eurocent >= 0";
        _euroAnteil = eurocent / 100;
        _centAnteil = eurocent % 100;
    }
    
    /**
     * Statische Methode zum erzeugen eines Geldbetrags 
     * @param eurocent Der Betrag in ganzen Euro-Cent
     * @return Gibt den Geldbetrag zurück
     */
    public static Geldbetrag erzeugeGeldbetrag(int eurocent)
    {
        return new Geldbetrag(eurocent);
    }

    /**
     * Gibt den Eurobetrag ohne Cent zurück.
     * 
     * @return Den Eurobetrag ohne Cent.
     */
    public int getEuroAnteil()
    {
        return _euroAnteil;
    }

    /**
     * Gibt den Centbetrag ohne Eurobetrag zurück.
     */
    public int getCentAnteil()
    {
        return _centAnteil;
    }

    /**
     * Liefert einen formatierten String des Geldbetrags in der Form "10,23"
     * zurück.
     * 
     * @return eine String-Repräsentation.
     */
    public String getFormatiertenString()
    {
        return _euroAnteil + "," + getFormatiertenCentAnteil();
    }

    /**
     * Liefert einen zweistelligen Centbetrag zurück.
     * 
     * @return eine String-Repräsentation des Cent-Anteils.
     */
    private String getFormatiertenCentAnteil()
    {
        String result = "";
        if (_centAnteil < 10)
        {
            result += "0";
        }
        result += _centAnteil;
        return result;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime + _centAnteil;
        result = prime * result + _euroAnteil;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof Geldbetrag)
        {
            Geldbetrag other = (Geldbetrag) obj;
            result = (_centAnteil == other._centAnteil)
                    && (_euroAnteil == other._euroAnteil);
        }
        return result;

    }

    /**
     * Gibt diesen Geldbetrag in der Form "10,21" zurück.
     */
    @Override
    public String toString()
    {
        return getFormatiertenString();
    }
    
    /**
     * Addiert 2 Geldbeträge und gibt einen neuen Geldbetrag zurück
     * @param betrag1 Der erste Geldbetrag
     * @param betrag2 Der zweite Geldbetrag
     * @return Der neue berechnete Geldbetrag
     */
    public static Geldbetrag addiere(Geldbetrag betrag1, Geldbetrag betrag2)
    {
        int euro = betrag1.getEuroAnteil() + betrag2.getEuroAnteil();
        int cent = betrag1.getCentAnteil() + betrag2.getCentAnteil();
        if (cent >= 100)
        {
            euro += 1;
            cent = cent % 100;
        }
        
        int eurocent =(euro*100) + cent;
        return erzeugeGeldbetrag(eurocent);
    }
    
    /**
     * Subtrahiert zwei Geldbeträge und gibt einen neuen Geldbetrag zurück
     * @param betrag1 Der erste Geldbetrag
     * @param betrag2 Der Geldbetrag, der vom ersten subtrahiert wird
     * @return Der neue berechnete Geldbetrag
     */
    public static Geldbetrag subtrahiere(Geldbetrag betrag1, Geldbetrag betrag2)
    {
        int euro = betrag1.getEuroAnteil() - betrag2.getEuroAnteil();
        int cent = betrag1.getCentAnteil() - betrag2.getCentAnteil();
       
        if (cent < 0)
        {
            euro -= 1;
            cent = 100 + cent;
        }
        
        int eurocent = (euro*100) + cent;
        return erzeugeGeldbetrag(eurocent);
    }
    
    /**
     * Multipliziert einen Geldbetrag mit einem Skalar und gibt einen neuen Geldbetrag zurück
     * @param betrag1 Der Geldbetrag, der multipliziert werden soll
     * @param skalar Skalar, mit dem multipliziert wird
     * @return Der neue berechnete Geldbetrag
     */
    public static Geldbetrag multipliziere(Geldbetrag betrag1, int skalar)
    {
        int euro = betrag1.getEuroAnteil() * skalar;
        int cent = betrag1.getCentAnteil() * skalar;

        if (cent >= 100)
        {
            euro += cent / 100;
            cent = cent % 100;
        }
        int eurocent = (euro*100) + cent;
        return erzeugeGeldbetrag(eurocent);
    }

}
