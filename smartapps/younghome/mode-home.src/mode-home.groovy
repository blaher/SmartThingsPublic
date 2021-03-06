definition(
	name: "Mode Home",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Does procedures required when coming home.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine@2x.png"
);

preferences {
	section('Select GPS') {
		input('gps', 'capability.switch', multiple: true);
	}
    
    section('Select Alexa Switch') {
		input('alexa', 'capability.switch', multiple: false);
	}
    
	section('Select Home Mode') {
		input('home_mode', 'mode');
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	
    states();
	subscribes();
}

def updated() {
	log.debug('Application updated');
    
    states();
    unsubscribe();
    subscribes();
}

def uninstalled() {
	log.debug('Application uninstalled');
    
    unsubscribe();
}

def states() {
	state.awake = false;
}

def subscribes() {
	subscribe(gps, 'switch.on', switchMode);
    subscribe(alexa, 'switch.on', switchAlexa);
	subscribe(location, 'mode', modeChanged);
}

def switchMode(evt) {
	log.debug('Someone has arrived.');
    
	//setLocationMode(home_mode);
    location.helloHome?.execute("I'm Back!")
}

def switchAlexa(evt) {
	if (!state.awake) {
        log.debug('Alexa reported someone woke up.');

        //setLocationMode(home_mode);
        location.helloHome?.execute("Good Morning!")
	} else {
    	log.debug('Someone is already awake.');
        
    	state.awake = false;
    }
}

def modeChanged(evt) {
    log.debug("Mode changed to: ${evt.value}");
    
    if (evt.value == home_mode) {
    	state.awake = true;
    	alexa.on();
    }
}