definition(
	name: "Aquarium FW Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Freshwater Aquarium Schedule",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
);

preferences {
	section('Select switch...') { // AquairumFWStrip
		input(name: 'strip', type: 'capability.switch', multiple: false);
	}
    
    section('Modes') {
		input('awake_mode', 'mode', title:'Awake');
		input('sleep_mode', 'mode', title:'Sleep');
		input('away_mode', 'mode', title:'Away');
	}
}

def installed() {
	log.debug('Installed with settings: ${settings}');
    
    states();
    subscribes();
	schedules();
    
	stopUnused();
}

def updated(settings) {
	log.debug('Application updated');
    
    states();
    subscribes();
	unschedule();
	schedules();
    
	stopUnused();
}

def states() {
	log.debug('Defining intial states');
    
    state.frontLight = false;
    state.backLight = false;
}

def subscribes() {
	log.debug('Subscribing to actions');

	subscribe(location, 'mode', checkMode);
}

def schedules() {
	log.debug('Scheduling times');

	schedule('0 0/15 * * * ?', checkTime);
}

def checkMode(evt) {
	log.debug('Checking mode');

	def calendar = Calendar.getInstance();
	def day = calendar.get(Calendar.DAY_OF_WEEK);
    
	if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
        if (evt.value == sleep_mode) {
            log.debug('You went to bed');

            stopLights(false);
        } else if (evt.value == away_mode) {
            log.debug('You left the house');

            stopLights(false);
        } else if (evt.value == awake_mode) {
            log.debug('You\'re home and awake');

            if (state.lights) {
                startLights(false);
            }
        }
    }
}

def checkTime() {
	log.debug('Checking time');
    
	def calendar = Calendar.getInstance();
	def day = calendar.get(Calendar.DAY_OF_WEEK);
	def hour = (new Date(now())).format('HH', location.timeZone);
    log.debug("Hour is ${hour} on weekday ${day}");
	
	switch (hour) {
		case '00':
			stopLights();
			stopUnused();
		break;
		case '06':
			switch (day) {
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.FRIDAY:
                case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					startLights();
				break;
			}
		break;
		case '10':
			switch (day) {
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.FRIDAY:
					stopLights();
				break;
			}
		break;
		case '17':
			startLights();
		break;
		case '21':
			switch (day) {
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
                case Calendar.SUNDAY:
					stopLights();
				break;
			}
		break;
	}
}

def startLights(auto = true) {
	log.debug('Turning on lights');
    
    if (auto) {
    	state.lights = true;
    }
	strip.on1();
    strip.on2();
}

def stopLights(auto = true) {
	log.debug('Turning off lights');
    
    if (auto) {
    	state.lights = false;
    }
	strip.off1();
    strip.off2();
}

def stopUnused() { // Used for other outlets for later use
	log.debug('Turning off unused outlets');
    
	strip.off3();
	strip.off4();
}