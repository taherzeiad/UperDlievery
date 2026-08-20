Been deep in building "Aber Driver" , a ride-hailing driver app , and wanted to share a bit of the process.

One thing I focused on from day one: building a real shared design system, not just screens that happen to look similar. Buttons, text styles, cells, dividers, segmented controls, message bars , all built once as reusable Compose components and reused everywhere. It's slower on day one, faster on every day after.

The app covers the full driver journey:
- Splash & onboarding (accept a job, track in real-time, earn money)
- Auth — sign up, sign in, phone verification
- Home, both offline and online states
- A swipe-up requests list for incoming trips
- Booking details with fare breakdown and quick actions (call, message, cancel)
- Turn-by-turn navigation to the pickup point
- In-app rider chat, built with proper message bubbles and timestamp handling

All of it on MVVM, clean architecture, and Jetpack Compose — treating every screen as a piece of a system, not an isolated task.

What I keep relearning on projects like this: the UI kit is the real productivity multiplier. Once it's solid, every new screen after it gets faster and more consistent , and that consistency is what users actually feel, even if they can't name it.

Excited to keep building this one out.

#JetpackCompose  #MVVM #MobileDevelopment
