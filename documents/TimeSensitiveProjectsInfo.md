Following there is info on the current projects on android track that are time sensitive 
regarding which challenges they faced and the solutions that were adopted to serve for 
future reference either for new projects or for their own maintenance.


- Secret Diary
  - requires timestamp on diary entries
  - problems:
    - there are several time apis in java/kotlin/android, not all of them are guaranteed 
    to be compatible with robolectric time emulation 
      - to not count on time emulation would require to use Thread.sleep, 
      which is not a good solution since it is constraining for tests and very slow
  - solution adopted:
    - author had already included "org.jetbrains.kotlinx:kotlinx-datetime:0.3.2" as dependency for the project
      - provides Clock.System
    - created a shadow for Clock.System
      - implementation for .now()
        - SystemClock.currentGnssTimeClock().instant().toKotlinInstant()
    - added observation stating that Clock.System.now() should be used for taking time cues
  - cons:
    - dependency included is on 0.x version
      - generally 0.x means the library is in a experimental phase or at least not yet mature
        - since the use case was very simple this was not considered a big issue
  - why not?
    - recommend use of SystemClock.currentGnssTimeClock()
      - it requires api 29 which is pretty high
        - current config is 20 - 29 (08/09/2022)
    - shadow System.currentTimeMillis()
      - it is not possible to shadow classes from java.lang. 
      On the other hand Robolectric will intercept calls to System.currentTimeMillis 
      coming from “instrumented code”, so it is possible to instrument the package 
      from the user solution, but it was preferred not doing so for this project.


- Music Player
  - requires playing songs and doing assertions based on playing time
  - requires animating some widgets
    - SeekBar, currentTimeTextView
  - time cues could be taken directly from MediaPlayer, no need for time apis
  - problems: 
    - robolectric tests default has the looper on paused mode, passage of time is done using 
    .idle(), .idleFor() or .runToTheEndOfTasks(), but it was found that actually some 
    instructions could increase the clock time by one millisecond involuntarily and these could
    pile up and produce significant shifts between actual and expected time
    - There are several options for implementing the animation of the widgets, some of them like 
    using Handler.postDelayed work without problems, some others like CountDownTimer can work if 
    given support by shadowing the class, others like java.util.Timer do not seem to work and are 
    unable to be shadowed.
    - it is not clear if it is possible to shadow classes coming from java.util, but it does not 
    seem possible from empirical experience.
  - solution:
    - measure time on test code in every method that could lead to increase in clock time as to 
    take that into account when doing assertions
    - created a shadow for CountDownTimer
      - uses Handler on implementation
    - added observation to use MediaPlayer for time cues
  - cons:
    - measuring time on test methods to deal with possible involuntary advances of the clock is somewhat costly
    
- Stopwatch
  - requires a timer counter
  - requires animating the counter
  - may require time cues depending on implementation choices
  - problems:
    - there are several time apis in java/kotlin/android, not all of them are guaranteed to be 
    compatible with robolectric time emulation
    - to not count on time emulation would require to use Thread.sleep, which is not a good 
    solution since it is constraining for tests and very slow
    - There are several options for implementing the animation of the widgets, some of them like 
    using Handler.postDelayed work without problems, some others like CountDownTimer can work 
    if given support by shadowing the class, others like java.util.Timer do not seem to work and 
    are unable to be shadowed.
    - it is not clear if it is possible to shadow classes coming from java.util, but it does not 
    seem possible from empirical experience.
  - solution:
    - created a shadow for CountDownTimer
      - uses Handler on implementation
    - instrumented user code
      - Robolectric will intercept calls to System.currentTimeMillis coming from 
      “instrumented code”, so it is possible to instrument the package from the user solution
      - requires test code to be located on a different package otherwise there will be a build failure
        - this kind of solution was hinted by robolectric mantainers responding one issue 
        as a possible solution, but it was not recommended
          - there was no info on why it was not recommended
    - added observations recommending use of thread schedulers coming from android and also 
    guaranteeing that System.currentTimeMillis should work



- related sources
  - https://stackoverflow.com/questions/31659771/robolectric-3-0-mocking-system-currenttimemillis
  - https://github.com/robolectric/robolectric/issues/1786
  - https://github.com/robolectric/robolectric/issues/6796#issuecomment-952475544
  - https://github.com/robolectric/robolectric/issues/7479
  - https://github.com/robolectric/robolectric/issues/6443
  - https://github.com/robolectric/robolectric/issues/1879
