# ClassMatePlus
A classroom productivity app featuring attendance tracking, doubt chat, notes sharing, and assignment reminders.

1. Problem Statement

In most educational institutions, students rely on manual methods or outdated systems to track their attendance, access important notes, share learning resources, and collaborate through doubts or discussions. This often leads to miscommunication, lost notes, inaccurate attendance tracking, and missed deadlines for assignments. Additionally, classrooms need a secure and restricted digital space where only enrolled students can access class-related information.

To address these challenges, there is a need for a unified mobile application that allows students to update their daily attendance, track their progress toward the mandatory 75% requirement, access shared notes, communicate doubts with peers, and receive reminders for upcoming deadlines. The system must also ensure classroom-specific access based on roll numbers to maintain data privacy and proper segmentation.


---

2. App Name

🔹 [To Be Decided] 


---

3. Idea Description

The proposed application is a classroom-focused student productivity tool designed to streamline attendance management, academic communication, and resource sharing within a controlled environment. Each student can log in using a validated roll-number-based authentication that restricts users to their respective classroom groups.

The app allows students to update their daily attendance based on the subjects scheduled. The system automatically calculates their percentage, identifies how many classes they can afford to miss while maintaining the required 75%, and provides instant updates.

Beyond attendance, the app offers a peer-to-peer chat system for doubt clarification, a shared space for uploading and accessing class notes or study materials, and a deadline reminder module for assignments and submissions. The app creates a complete academic ecosystem that enhances student collaboration, reduces confusion, and helps maintain consistent academic performance.


---

4. Basic Features

A. Attendance Tracking System

Students can update daily attendance manually based on the subjects attended.

Automatic calculation of attendance percentage.

Shows how many more classes the student can skip while staying above 75%.

Displays warning notifications for low attendance.


B. Classroom-Based Login Restriction

Login permitted only for valid roll numbers belonging to a specific classroom.

Each classroom has a unique group; students cannot join other classroom groups.

Ensures privacy and correct data segmentation.


C. Doubt-Solving Chat Box

A live chat space for students to ask and answer doubts.

Works like a simple messaging platform.

Encourages collaborative learning among classmates.


D. Notes & Document Sharing Space

Upload and share class notes, PDFs, images, and study materials.

Available for all classroom members.

Helps absent students access missed content.


E. Assignment & Deadline Reminder Timeline

Shared timeline for posting assignment deadlines.

Shows countdown to due dates (e.g., “3 days left”).

Upload access may be restricted to selected members (e.g., class representative).



---

5. Tech Stack

Frontend

Android Studio

Kotlin for app development

XML for UI design


Backend (Optional / Future Expansion)

Firebase Authentication (for login + roll number verification)

Firebase Firestore / Realtime Database (for attendance, chat, notes, deadlines)

Firebase Storage (for notes and document uploads)


Additional Frameworks

Material Design Components

RecyclerView, ViewModel, LiveData

Coroutines for async operations



---

6. Development Timeline

Phase	Task	Duration

Phase 1	Requirements collection & UI sketches	2–3 days
Phase 2	User authentication & roll-number-based login	3–4 days
Phase 3	Attendance module implementation	4–5 days
Phase 4	Chat system for doubt solving	4–6 days
Phase 5	Notes/document sharing module	3–5 days
Phase 6	Assignment deadline timeline	3–4 days
Phase 7	Integration, testing & refinement	5–7 days


Total Estimated Time: ~25–30 days


---

7. Team Members 

Akshaya Regidi 
Anusha 
Sindhu Nayana
