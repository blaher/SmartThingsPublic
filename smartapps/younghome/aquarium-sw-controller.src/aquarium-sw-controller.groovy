definition(
	name: 'Aquarium SW Controller',
	namespace: 'younghome',
	author: 'Benjamin J. Young',
	description: 'Controls Saltwater Aquarium Schedule',
	category: 'Convenience',
	iconUrl: 'https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png',
	iconX2Url: 'https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png'
);

preferences {
	section('Select switch...') { // AquairumSWStrip
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
    
	startUsed();
}

def updated(settings) {
	log.debug('Application updated');
    
    states();
    subscribes();
	unschedule();
	schedules();
    
	startUsed();
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

            stopFrontLight(false);
            stopBackLight(false);
        } else if (evt.value == away_mode) {
            log.debug('You left the house');

            stopFrontLight(false);
            stopBackLight(false);
        } else if (evt.value == awake_mode) {
            log.debug('You\'re home and awake');

            if (state.frontLight) {
                startFrontLight(false);
            }
            if (state.backLight) {
                startBackLight(false);
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
			stopFrontLight();
			stopBackLight();
			startUsed();
		break;
		case '06':
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.FRIDAY:
					startBackLight();
				break;
			}
		break;
		case '10':
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.FRIDAY:
					stopBackLight();
				break;
				case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					startBackLight();
				break;
			}
		break;
		case '15':
			switch (day){
				case Calendar.SUNDAY:
					startFrontLight();
				break;
			}
		break;
		case '16':
			startFrontLight();
			startBackLight();
		break;
		case 17:
			switch (day){
				case Calendar.SUNDAY:
					stopBackLight();
				break;
			}
		break;
		case '18':
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.SATURDAY:
					stopBackLight();
				break;
			}
		break;
		case '20':
			switch (day){
				case Calendar.FRIDAY:
					stopBackLight();
				break;
			}
		break;
		case '22':
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.SUNDAY:
					stopFrontLight();
				break;
			}
		break;
	}
}

def startFrontLight(auto = true) {
	log.debug('Turning on front light');
    
    if (auto) {
    	state.frontLight = true;
    }
	strip.on1();
}

def stopFrontLight(auto = true) {
	log.debug('Turning off front light');
    
    if (auto) {
    	state.frontLight = false;
    }
	strip.off1();
}

def startBackLight(auto = true) {
	log.debug('Turning on back light');
    
    if (auto) {
    	state.backLight = true;
    }
	strip.on2();
}

def stopBackLight(auto = true) {
	log.debug('Turning off back light');
    
    if (auto) {
    	state.backLight = false;
    }
	strip.off2();
}

def startUsed() { // Used for other outlets for later use
	log.debug('Turning on used outlets');
    
	strip.on3();
	strip.on4();
}