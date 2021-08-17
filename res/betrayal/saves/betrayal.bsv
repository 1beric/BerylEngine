layer; name=models; type=model
entity; name=GameController; layer=models
component; name=GameController; prefix=betrayal/controllers; entity=GameController

layer; name=hud; type=gui
entity; name=HudController; layer=hud
component; name=HUDController; prefix=betrayal/controllers; entity=HudController

layer; name=mainMenu; type=gui
entity; name=MainController; layer=mainMenu
component; name=MainMenuController; prefix=betrayal/controllers; entity=MainController

layer; name=settingsMenu; type=gui
entity; name=SettingsController; layer=settingsMenu
component; name=SettingsController; prefix=betrayal/controllers; entity=SettingsController

layer; name=layerTransitions
entity; name=LayerTransitioner; layer=layerTransitions
component; name=LayerSwitcher; prefix=betrayal; entity=LayerTransitioner;

# layer; name=_____; type=_____
# entity; name=_____; layer=_____
# component; name=_____; prefix=_____; entity=_____