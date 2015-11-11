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
}

def installed() {
	log.debug("Installed with settings: ${settings}");
}

def updated() {
	log.debug('App updated');
    
    states();
    schedules();
}

def states() {
	log.debug('Defining intial states');
    
    state.isOn = false;
}

def schedules() {
	log.debug('Scheduling events');

    schedule('0 0 5 4 6 ?', lightsOn);
    schedule('0 0 8 4 6 ?', lightsOff);
}

def lightsOn() {
	log.debug('Lights on');

	state.isOn = true;
    state.lightAuto = false;
    
    lightsOne.setColorTemperature(0);
    lightsTwo.setColorTemperature(0);
    lightsThree.setColorTemperature(0);
    
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
}

def stepOne() {
	log.debug('Step one...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOne.setColor(hex: '#ff0000');
        lightsTwo.setColor(hex: '#ffffff');
        lightsThree.setColor(hex: '#0000ff');

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

		unschedule(stepThree);
        runIn(2, stepThree);
    }
}

def stepThree() {
	log.debug('Step three...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOne.setColor(hex: '#ffffff');
        lightsTwo.setColor(hex: '#0000ff');
        lightsThree.setColor(hex: '#ff0000');

		unschedule(stepOne);
        runIn(2, stepOne);
    }
}