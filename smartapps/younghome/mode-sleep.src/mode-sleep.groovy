definition(
	name: "Mode Sleep",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Does procedures required when going to bed.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/good-night.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/good-night@2x.png"
);

preferences {
	section('Select Alexa Switch') {
		input('alexa', 'capability.switch', multiple: false);
	}

	section('Select Night Mode') {
		input('night_mode', 'mode');
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
    subscribe(alexa, 'switch.off', switchMode);
	subscribe(location, 'mode', modeNight);
}

def switchMode(evt) {
	log.debug('Alexa reported to go to bed.');
	
    //setLocationMode(night_mode);
    location.helloHome?.execute("Good Night!")
}

def modeNight(evt) {
    log.debug("Mode changed to: ${evt.value}");
    
    if (evt.value == night_mode) {
    
    }
}