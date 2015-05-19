package game.controls;

/**
 * Rappresenta un bottone che può eseguire un'azione, incapsulata in un ButtonAction.
 * @author Stefano De Pace
 */
public class Button {

    private String name;
    private ButtonAction action;
    private boolean continuousPressing;
    private boolean needToBeReady;

    /**
     * Crea un nuovo Button.
     * @param name Nome del Button.
     * @param action ButtjonAction che rappresenta l'azione da eseguire quando viene premuto.
     * @param continuousPressing Parametro che indica se il Button funziona con pressione continua.
     * @param needToBeReady Pamrametro che marca un Button che può essere disabilitato all'occorrenza.
     */
    public Button(String name, ButtonAction action, boolean continuousPressing, boolean needToBeReady) {
        this.name = name;
        this.action = action;
        this.continuousPressing = continuousPressing;
        this.needToBeReady = needToBeReady;
    }

    /**
     * Restituisce il nome del Button dato alla creazione.
     * @return nome del Button dato alla creazione.
     */
    public String getName() {
        return name;
    }

    /**
     * Esegue l'azione incapsulata nel ButtonAction.
     * @param parameter Eventuale parametro per l'azione.
     */
    public void execute(Object parameter) {
        action.action(parameter);
    }

    /**
     * Restituisce true se il Button funziona con pressione continua.
     * @return Restituisce true se il Button funziona con pressione continua.
     */
    public boolean isContinuousPressing() {
        return continuousPressing;
    }

    /**
     * Restituisce se il Button può essere disabilitato.
     * @return Restituisce se il Button può essere disabilitato.
     */
    public boolean isNeedToBeReady() {
        return needToBeReady;
    }

}
