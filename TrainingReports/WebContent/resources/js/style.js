/*

Please refer to readme.html for full Instructions



Text[...]=[title,text]



Style[...]=[TitleColor,TextColor,TitleBgColor,TextBgColor,TitleBgImag,TextBgImag,TitleTextAlign,TextTextAlign, TitleFontFace, TextFontFace, TipPosition, StickyStyle, TitleFontSize, TextFontSize, Width, Height, BorderSize, PadTextArea, CoordinateX , CoordinateY, TransitionNumber, TransitionDuration, TransparencyLevel ,ShadowType, ShadowColor]

*/



var FiltersEnabled = 1 // if your not going to use transitions or filters in any of the tips set this to 0



Text['Attended']=["Attended","Colleague attended entire product training session."];

Text['Transitional Training']=["Transitional Training","Additional Training is required.  Please contact Training Logistics for more information (1-866-4LD-2WIN or traininglogistics@pfizer.com)."];

Text['Regional Training']=["Regional Training","FFT Regional Training is required.  Please contact your ARM."];

Text['Absent: Training Needed']=["Absent: Training Needed","Training is required.  Please contact your Regional Sales Office."];

Text['On Leave']=["On Leave","Training is required.  Please contact your Regional Sales Office."];

Text['Demonstrated Competence']=["Demonstrated Competence (DC)","Colleague demonstrated an ability to successfully meet the expectations of the Sales Call Evaluation (SCE)."];

Text['Needs Improvement']=["Needs Improvement (NI)","Colleague received an NI score on two or more of the selling skills--as defined in the Pfizer Selling Model (NI + NI = NI) or received an NI and UN on any of the selling skills (NI + UN = NI)."];

Text['Unacceptable']=["Unacceptable (UN)","Colleague received a UN for any two selling skills--as defined in the Pfizer Selling Model (UN + UN = UN) or received a no for Healthcare Law compliant."];

Text['Not Complete sce']=["Not Taken","Colleague did not complete Sales Call Evaluation (SCE)."];

Text['Complete o']=["Complete","Colleague completed all three requirements:  Pedagogue Exams, Attendance, and Sales Call Evaluation."];

Text['Not Complete o']=["Not Complete","Colleague did not complete one or all of the requirements:  Pedagogue Exams, Attendance, or Sales Call Evaluation."];

Text['examRecommended']=["Recommended","Colleague did not score &ge; 80% on exam.  Recommended coaching:  Review Learning System modules via LSO and complete progress check questions."];

Text['Test Required']=["Test Required","Colleague did not complete Pedagogue exam.  Inform colleague to take exam.  If exam cannot be accessed (technical issues), please contact Dendrite Help Desk."];

Text['Not Required']=["Not Required","Colleague received &ge; 80% on exam.  No action required."];

Text['sceRecommended']=["Recommended","Colleague received a NI on Sales Call Evaluation. Coaching Recommendations:  Click SCE score to review areas of development--review Guest Trainers comments and continue role-playing on field rides."];

Text['Required']=["Required","Colleague received a score of UN on Sales Call Evaluation. Coaching recommendations:  Click SCE score and review Guest Trainers comments, review promotional materials, role-play, and administer new SCE."];

Text['']=["",""];


Text['Complete POA']=["Complete","Colleague has completed the preparation work."];

Text['Not Complete POA']=["Not Complete","Colleague has not completed the preparation work."];
Text['On Leave POA']=["On Leave","Colleague is currently on leave."];



Style[0]=["white","black","#1f61a9","#d2e5f0","","","","","","","right","","0.8em","0.8em",210,"",2,2,10,10,"","","","",""]

Style[1]=["white","black","#cc0000","#FFCC99","","","","","","","center","","","",200,"",2,2,10,10,"","","","",""]

Style[2]=["white","black","#cc0000","#FFCC99","","","","","","","left","","","",200,"",2,2,10,10,"","","","",""]

Style[3]=["white","black","#cc0000","#FFCC99","","","","","","","float","","","",200,"",2,2,10,10,"","","","",""]

Style[4]=["white","black","#cc0000","#FFCC99","","","","","","","fixed","","","",200,"",2,2,150,400,"","","","",""]

Style[5]=["white","black","#cc0000","#FFCC99","","","","","","","","sticky","","",200,"",2,2,10,10,"","","","",""]

Style[6]=["white","black","#cc0000","#FFCC99","","","","","","","","keep","","",200,"",2,2,10,10,"","","","",""]

Style[7]=["white","black","#cc0000","#FFCC99","","","","","","","","","","",200,"",2,2,40,10,"","","","",""]

Style[8]=["white","black","#cc0000","#FFCC99","","","","","","","","","","",200,"",2,2,10,50,"","","","",""]

Style[9]=["white","black","#cc0000","#FFCC99","","","","","","","","","","",200,"",2,2,10,10,51,0.5,75,"simple","gray"]

Style[10]=["white","black","black","white","","","right","","Tahoma","cursive","center","",3,4,200,20,5,10,10,0,50,1,70,"complex","gray"]

Style[11]=["white","black","#cc0000","#FFCC99","","","","","","","","","","",200,"",2,2,10,10,51,0.5,45,"simple","gray"]

Style[12]=["white","black","#cc0000","#FFCC99","","","","","","","","","","",200,"",2,2,10,10,51,1,0,"",""]



applyCssFilter()



