Add entries to this file summarising each project milestone. Don't forget that these need to have been discussed with your TA before the milestone deadline; also, remember to commit and push them by then!

## Milestone 1
Idea Description: 
- Idea: A tool that creates UML/Class diagrams to show Class Coupling of a Java project
- Visualization: We can use a webpage to showcase diagrams or solely create UML/Class diagrams for highlighting classes that are coupled with corresponding colours.
- Input data: Project files and/or folder(s) containing Java files or other folders, which contain java files or are possibly empty
- How would we show coupling? One way is to measure how many methods of one class calls another class’ methods.
- What problem does the tool try to address? This tool would be helpful if the programmer has to deal with hundreds of files, so it'll make it easier to get the bigger picture and understand the flow. It is also useful if the programmer needs to focus on a certain area of a large project.

TA Feedback: 
- TA said our idea seemed good and needed to double-check with Profressor Alex.


Follow Up Tasks & Features: 
- Figure out how to show coupling and create the diagrams in an efficient/organized manner
- We can take into account of how the diagrams will be colour-coded. For example, if the programmer wants to only look at the coupling of a specific class, this class and its coupled classes will be highlighted in one colour while the rest will greyed out/less visible. That way, the area that the programmer is currently focusing on stands out from the rest.
- Meet up next week to discuss roles and more details on our plans

## Milestone 2 
Planned division of main responsibilities:
1. Visualization: Yukie + Wewic 
2. Program Analysis: Avi + Jeremey (majority) & Yukie + Wewic (assuming visualization is simple enough)

Roadmap:
- Before Milestone 3 (Nov 13): create language design, find user studies and record their responses, think about how to integrate our language design with visualization
- Before Milestone 4 (Nov 20): complete majority of implementation, find more users for final user study
- Before Project Due Date (Nov 30): once implementation is done, meaning our program is working as planned as integrated with the visualization, we will send it out for final user study a few days prior the due date. Then, take in feedback and improve implementation.

Summary of progress so far:
- Met up with TA to come with a unique idea from Class Coupling idea
- We went through 3 new ideas (memory optimizaiton, runtime analysis, poor programming practices/code smells)
- some feedback about the 3 new ideas:
1. Memory optimizaiton: finding ASTs might be difficult for C/C++, need to find a middle ground between dynamic and static analysis
2. Runtime analysis: Add more performance-based aspects such as # times methods are called and donsider what data is being passed
3. Poor programming practices/code smells: almost like a linter but linter looks at formatting, not at what the code does, so focus on that more.
- Conclusion: integrate one or more of these ideas to Class Coupling to make it more unique. An example would be ... "what kind of programming practices are found within UML classes?". 
- Need to meet up with team next week to discuss this idea in full and complete milestone 3 tasks

## Milestone 3
Mockup of analysis design/visualization:
![visual1](https://i.gyazo.com/72786cbc795c9011b9b8d96e49b73cfc.png)
- Each class will be represented by a circle of a unique color whose size will be proportional to the size of the class (number of fields and methods).
- The class name will also be displayed within the circle
- Fields will be represented by shapes inside of the class circle, and methods will be shapes outside of the class circle attached by solid lines. 
- The shape of the field or method can be used to convey useful information such as visibility modifiers (private and public).

![visual2](https://i.gyazo.com/0bd09175d85c59b3eeedacfab5bb6938.png)
- Interactive aspect: The user will be able to click on a class to highlight all classes, fields and methods that depend on it (Image 1).
- Alternatively, the user will also be able to click on a method of any class to highlight classes on which that particular method depends, methods from other classes that call or are called by that given method, and finally fields within its own class that the method uses.


Notes about first user study: 
- User study #1 (CS College Student): 
1. I think the project makes a lot of sense, it looks really fun and interesting tbh

2. Maybe as a stretch goal, you have also output a written report containing statistics or data about the visualization. For example, maybe how many lines of code are in each class or how many methods or fields are in each class. Also, Is there a chance you'll run out of  colors for the visualization? That's a concern theoretically if the program is big and then some colors might look really similiar.

3. I think the project is a great way to learn OOP and Java, I like it! (although I dislike working with Java)

- User study #2 (Gr. 12 applying for CS Program):
1. I'd say the concept of the whole project does seem to make sense, the idea of being able to see what classes/attributes are associated with each other is rly useful. I don't think I have enough knowledge to comment on any problems I'd foresee with the implementation but based on stuff IDEs can do I think that wouldn't be a problem. Also I think a graphical visualization like this is much better than the way an IDE provides information :D

2. I think maybe adding the number of fields and methods each class has next to the name when displaying it would be somewhat useful, especially if larger classes have a lot of stuff associated with it and just trying to count the number of shapes gets a bit confusing since (this is just for me tho idk about for others) I tend to lose track of counting small things like this

3. I really like the simplicity of the idea, being able to provide critical information in a very easy to understand manner will be super helpful! There's not really anything that I dislike about it tbh
Sry if I can't provide any further information on this as that's pretty much the limit of my knowledge on the topic haha, although please lmk if you need testers later down the line when this becomes a working program, I'd love to try it out!

- User study #3: 
1. What made sense to me
 - The basic idea of this project : Visualize the design of a system or segments of it is a well known, frequent, and crucial necessity.
 - Also isolating the interactions of specific methods or fields : for the same reasons.
 - Representing information using shapes and colors : Simple visual semantics allows quick and intuitive readability.
 - Documenting the design of a project in this way automatically : Manually doing this step is laborious and facilitates the introduction of mistakes.
 
2. What didn't make sens (might be irrelevant since I made some assumptions)
 - How can the shapes representing methods and fields be identified without the corresponding name?
 - Would it always be preferable that the class with more attributes appears bigger?
 
3. Possible improvements
 - The most standard visual modeling languages' large size is known to hinder learning. In companies, it often ends up being used with the individual authors' interpretation and sense of what is intuitive. Maybe a possibility to customize the "language's features" would be comfortable (I'm thinking about Square and circle associated with Private and Public).
 - I find myself questioning the use of colors to differentiate classes from each other. Is it necessary? And couldn't colors, a powerful visual tool, be used more efficiently?
 
4. What I like about this idea 
 - Having a tool that can do this operation automatically. Even without thinking about making mistakes trying to do it manually, I would bet money that developers would use a lot more often a visual representation as a reference because it's quicker and effortless.
 - I like the description of the beneficial points of having a visual representation: "getting a feel of OOP principles," "understanding or controlling coupling and cohesion in a system design" ...
 
5. What I dislike or am worried about (again with some assumptions)
 - Worried that the representation choices will make the visual representation very heavy for big projects, or simply for classes with lots of attributes.
 - Don't like that the list of attributes for a class seems to be randomly spread in and out arround it. I usually like to have some rules of thumb to be able to search for one specific attribute especially when it lies among many.

Any changes to original analysis/visualisation design: 
- things to consider: colour representation if project is very large, display # of classes/methods/fields/etc., visualize stats/data about program (ex. # lines of code in each class).

## Milestone 4
Status of implementation:
- Jeremy got the Java parser working, however Avi thinks Java Swing might be difficult to use for our visualization aspect. So we will need to discuss what language our implementation will be in. 
- Nov 24th Update: Although our group is inexperienced with React, we have decided to go along with it for our visualization aspect. After discussing with Alex and Kathaerine, we believe it would be much better to learn and implement with React than use Java Swing or some other Java libraries.

Plans for final user study:
- contact previous users and new users to test our program
- receive feedback and make changes to our implementation if required

Planned timeline for the remaining days:
- Before Nov 25th: Complete implementation before Nov 25th with some tests
- Nov 25th: Complete final user study
- Nov 25th-26th: Make changes according to final user study feedback
- Nov 26th-30th: Test our implementation more and make the according fixes
