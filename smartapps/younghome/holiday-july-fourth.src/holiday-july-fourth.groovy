definition(
	name: "Holiday July Fourth",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Flashes the entire house's colors, and plays 1812 Overture over sonos.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Developers/smart-light-timer.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Developers/smart-light-timer@2x.png"
);


preferences {
	section('Select Lights') {
		input('lightsOne', 'capability.colorControl', title:'One', multiple:true);
		input('lightsTwo', 'capability.colorControl', title:'Two', multiple:true);
		input('lightsThree', 'capability.colorControl', title:'Three', multiple:true);
	}
    
    section('Select fishtank...') {
		input(name: 'strip', type: 'capability.switch', multiple: false);
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
    
    states();
    schedules();
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

    schedule('0 0/10 5 4 6 ?', lightsOn);
    schedule('0 0/15 9 4 6 ?', lightsOff);
}

def lightsOn() {
	log.debug('Lights on');
    
    strip.off1();
    strip.off2();

	state.isOn = true;
    state.lightAuto = false;
    
    lightsOne.setColorTemperature(0);
    lightsTwo.setColorTemperature(0);
    lightsThree.setColorTemperature(0);
    
    lightsOne.setLevel(100);
    lightsTwo.setLevel(100);
    lightsThree.setLevel(100);
    
    stepOne();
}

def lightsOff() {
	log.debug('Lights off');

	state.isOn = false;
    state.lightAuto = true;
    
    lightsOne.setColor(hex: '#000000');
    lightsTwo.setColor(hex: '#000000');
    lightsThree.setColor(hex: '#000000');
    
    lightsOne.setColorTemperature(100);
    lightsTwo.setColorTemperature(100);
    lightsThree.setColorTemperature(100);
    
    lightsOne.setLevel(0);
    lightsTwo.setLevel(0);
    lightsThree.setLevel(0);
}

def stepOne() {
	log.debug('Step one...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOne.setColor(hex: '#ff0000');
        lightsTwo.setColor(hex: '#ffffff');
        lightsThree.setColor(hex: '#0000ff');
        
        lightsOne.setLevel(100);
        lightsTwo.setLevel(100);
        lightsThree.setLevel(100);

		unschedule(stepTwo);
        runIn(2, stepTwo);
    }
}

def stepTwo() {
	log.debug('Step two...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOne.setColor(hex: '#0000ff');
        lightsTwo.setColor(hex: '#ff0000');
        lightsThree.setColor(hex: '#ffffff');
        
        lightsOne.setLevel(100);
        lightsTwo.setLevel(100);
        lightsThree.setLevel(100);

		unschedule(stepThree);
        runIn(5, stepThree);
    }
}

def stepThree() {
	log.debug('Step three...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOne.setColor(hex: '#ffffff');
        lightsTwo.setColor(hex: '#0000ff');
        lightsThree.setColor(hex: '#ff0000');
        
        lightsOne.setLevel(100);
        lightsTwo.setLevel(100);
        lightsThree.setLevel(100);

		unschedule(stepOne);
        runIn(5, stepOne);
    }
}