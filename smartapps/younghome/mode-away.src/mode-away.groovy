definition(
	name: "Mode Away",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Does procedures required when leaving.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage@2x.png"
);

preferences {
	section('Select GPS') {
		input('gps', 'capability.switch', multiple: true);
	}
    
	section('Select Away Mode') {
		input('away_mode', 'mode');
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	
	subscribes();
}

def updated() {
	log.debug('Application updated');
    
    unsubscribe();
    subscribes();
}

def uninstalled() {
	log.debug('Application uninstalled');
    
    unsubscribe();
}

def subscribes() {
	subscribe(gps, 'switch.off', switchMode);
	subscribe(location, 'mode', modeChanged);
}

def switchMode(evt) {
	log.debug('Someone has left.');
    
    def currSwitches = gps.currentSwitch;

    def onSwitches = currSwitches.findAll {
    	switchVal -> switchVal == "on" ? true : false;
    }

    if (onSwitches.size() == 0) {
	    //setLocationMode(away_mode);
        location.helloHome?.execute("Goodbye!")
	}
}

def modeChanged(evt) {
    log.debug("Mode changed to: ${evt.value}");
    
    if (evt.value == away_mode) {
    
    }
}