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
Mockup of analysis design:
- Our program design is to analyze code where we can split up classes (class coupling) and also detect structural design patterns such as composite design pattern. Since Structural Design Patterns provides a simple  way to realize relationships between entities, it would be great to merge this concept and class coupling.  We know we have to look for sequence of calls to determine design patterns. As for class coupling, we will determine which classes uses extends, interfaces, etc.

Mockup of visualisation: 
- UML diagrams is our still base idea for visualization as mentioned in Milestone 1. However, we will need to brainstorm more ideas to make our visualizaiton completely unique, so that is doesn't look like a UML diagram at first glance. 

Notes about first user study: 
- User study #1: finds that the visualization aspect will be useful for programmers who are unfamiliar with a new project, believes that it is a good idea to "determine which classes uses extends, interfaces, etc" to detect class coupling, and seems very abstract at the moment so it is kind of confusing

Any changes to original analysis/visualisation design: 
- will need to consider if we want to keep the "detect structural design patterns" aspect in our program because this would be difficult to analyze/find in given code
