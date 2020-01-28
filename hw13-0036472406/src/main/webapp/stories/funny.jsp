<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.Random"%>
<html>
	<head>
		<style type="text/css">
			@import url("/webapp2/style");
			
			body {
				color: 
					<%
						Random rand = new Random();
						out.write(String.format(
							"rgb(%d, %d, %d)",
							rand.nextInt(256),
							rand.nextInt(256),
							rand.nextInt(256)
						));
					%>;
			}
		</style>
	</head>
	
	<body>
		<h2>Funny story (source: reddit)</h2>
		<p>Let me tell you that I have made a bad mistake this evening.</p>
		<p>My girlfriend (who let me tell you is only my 2nd girlfriend of all time) said I am "invited to dinner" with her and her parents. I was very aghast, nervous, and bashful to be invited to such a situation. But I knew it must be done.</p>
		<p>I met them nicely, I should tell you, and it started off in a good way. The idea slapped my mind that I should do a comic bit, to make a good impression and become known to them as a person who is amusing.</p>
		<p>When I saw that baked potatoes were served I got the idea that it would be very good if I pretended I did not know what potatoes was. That would be funny.</p>
		<p>Well let me tell you: backfired on my face. I'll tell you how.</p>
		<p>So first when the potato became on my plate, I acted very interesting. I showed an expression on my face so as to seem that I was confused, astounded but in a restrained way, curious, and interested. They did notice, and seemed confused, but did not remark. So I asked "This looks very interesting. What is this?"</p>
		<p>They stared at me and the mother said "It's a baked potato." And I was saying "Oh, interesting, a baked....what is it again?"</p>
		<p>And she was like "A potato."</p>
		<p>And I was like "A 'potato', oh interesting. Never heard of a potato, looks pretty good."</p>
		<p>And then they didn't see I was clowning, but thought I really did not know what is a potato. So I knew I would be very shamed, humiliated, depressed, and disgusted if I admitted to making a bad joke, so what I did was to act as if it was not a joke but I committed to the act of pretending I didn't know what a potato is.</p>
		<p>They asked me, VERY incredulous, did I really not know what a potato is? That I never heard of a potato. I went with it and told them, yes, I did not ever even hear of a potato. Not only had I never eaten a potato I had never heard the word potato.</p>
		<p>This went on for a bit and my girlfriend was acting very confused and embarrassed by my "fucked up antics", and then the more insistent I was about not knowing what a potato is was when them parents starting thinking I DID know what a potato was.</p>
		<p>Well let me tell you I had to commit 100% at this point. When I would not admit to knowing what a potato was, the father especially began to get annoyed. At one point he said something like "Enough is enough. You're fucking with us. Admit it." And I said "Sir, before today I never heard of a potato. I still don't know what a potato is, other than some kind of food. I don't know what to tell you."</p>
		<p>Well let me tell you he got very annoyed. I decided to take a bite of the potato, and when I did I made a high pitched noise and said "Taste's very strange!"</p>
		<p>That is when the father started yelling at me, and the mother kept saying "What are you doing?" and my girlfriend went to some other room.</p>
		<p>Finally the father said I should "Get the fuck out of his house" and I said it was irrational to treat me like this just because I never heard of a potato before. Well let me tell you he didn't take that kindly.</p>
		<p>Now in text messages I have been telling my girlfriend I really don't know what a potato is. The only way I can ever get out of this is for them to buy that I don't know what a potato is.</p>
		<p>I wish I never started it but I can't go back. I think she will break up with me anyway.</p>
	</body>
</html>
