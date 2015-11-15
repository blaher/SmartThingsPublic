definition(
	name: "Holiday Christmas",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Flashes the entire house's colors, and plays random christmas music over sonos while away and movement is detected in front.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Developers/smart-light-timer.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Developers/smart-light-timer@2x.png"
);


preferences {
	section('Select Lights') {
		input('lightsOdd', 'capability.colorControl', title:'Odd', multiple:true);
		input('lightsEven', 'capability.colorControl', title:'Even', multiple:true);
	}
    
    section('Select fishtank...') {
		input(name: 'strip', type: 'capability.switch', multiple: false);
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
    
    states();
    schedules()
}

def updated() {
	log.debug('App updated');
    
    states();
    unschedule();
    schedules();
}

def uninstalled() {
	log.debug('Application uninstalled');
    
   unschedule();
}

def states() {
	log.debug('Defining intial states');
    
    state.isOn = false;
}

def schedules() {
	log.debug('Scheduling events');

    schedule('0 0/15 5-6 1-25 12 ?', lightsOn);
    schedule('0 0/15 8 1-25 12 ?', lightsOff);
}

def lightsOn() {
	log.debug('Lights on');
    
    strip.off1();
    strip.off2();

	state.isOn = true;
    state.lightAuto = false;
    
    lightsOdd.setColorTemperature(0);
    lightsEven.setColorTemperature(0);
    
    stepOne();
}

def lightsOff() {
	log.debug('Lights off');

	state.isOn = false;
    state.lightAuto = true;
    
    lightsOdd.setColor(hex: '#000000');
    lightsEven.setColor(hex: '#000000');
    
    lightsOdd.setColorTemperature(100);
    lightsEven.setColorTemperature(100);
}

def stepOne() {
	log.debug('Step one...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOdd.setColor(hex: '#ff0000');
        lightsEven.setColor(hex: '#00ff00');

		unschedule(stepTwo);
        runIn(5, stepTwo);
    }
}

def stepTwo() {
	log.debug('Step two...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOdd.setColor(hex: '#00ff00');
        lightsEven.setColor(hex: '#ff0000');

		unschedule(stepOne);
        runIn(5, stepOne);
    }
}