# Mood Tracking and Prediction Application 
Created as my final project during my Degree program

# Abstract
Many mobile applications for journaling and reflecting upon your mood and mental health have
become popular in recent years as the importance of mental health has entered the current zeitgeist. These applications however rely on users actively engaging with the application to record
their feeling, a common feature of low moods is predisposition to not be actively engaged limiting
these applications usefulness.
This project creates an application that builds upon previous work to create mood tracking software
that integrates with activity tracking functions. Data is collected from the accelorometer, as well as
phone usages statistics and are used to measure affects from the positive affect, negative affect
schedule (PANAS). Additional GPS location data is collected to give more context to the affect data
collected. We used a Bayesian inference to predict the users mood based on their current activity
that day compared to their history of activity and moods. We then used this information to give them
suggestions on what to do if the mood is predicted to be a poor one.
The application was with limited testing found to have greater predictive power that previous research but was rated as lower in it’s usefulness in improving mood.

# Use Case Diagram
<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/UseCase.png?raw=true">

# Initial mock up
<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/mock%20up.png?raw=true">

# Bayesian Solution
<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/Equation.png?raw=true">

# Database Design
<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/Database.png?raw=true">

# Screenshots
<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/1.png?raw=true">

<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/2.png?raw=true">

<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/3.png?raw=true">

<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/4.png?raw=true">

<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/5.png?raw=true">

<img src="https://github.com/SimonLongstaff/MoodPredictor/blob/master/6.png?raw=true">
