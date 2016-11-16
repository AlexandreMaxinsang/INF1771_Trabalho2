%-------------------------------------------------------------------------------
% Perception_Tell_KB : 
% inform the Knowledge Base(KB) of our agent about the features of the world ,
%
perception_tell_KB([Stench,Bleeze,Glitter,Bump,Flash,Powerup]):-
	add_enemy_KB(Stench),
	add_pit_KB(Bleeze),
	add_gold_KB(Glitter),
	add_wall_KB(Bump),
	add_teletransport_KB(Flash),
	add_powerup_KB(Powerup),
	add_saferoom_KB(Stench,Bleeze,Flash),
	add_certain_enemy_KB,
	add_certain_pit_KB,
	add_certain_teletransport_KB,
	!.
	
% uptade our perceptions 
	
powerup :-
	agent_location(L),
	powerup_location(L),
	!.
	
stenchy :-
	enemy_location(_,_,L1),
	agent_location(L2),
	adjacent(L1,L2),
	!.

bleezy :- 
	pit_location(L1),
	agent_location(L2), 
	adjacent(L1,L2),
	!.

glittering:-
	agent_location(L),
	gold_location(L),
	!.

bumped:-
	agent_location(L),
	wall_location(L),
	!.

flash:-
	teletransport_location(L1),
	agent_location(L2),
	adjacent(L1,L2),
	!.
		
% update our knowledge about enemy presence

add_enemy_KB(yes) :-
	stenchy,
	agent_location(L1),				% I don't know if I'm in a enemy place
	location_toward(L1,[1,0],L2),	% And It's possible there are enemy in 
	no(has_enemy(L2)),
	assume_enemy(L2),				% each adjacent room. <=|
	location_toward(L1,[0,1],L3),
	no(has_enemy(L3)),	
	assume_enemy(L3),
	location_toward(L1,[-1,0],L4),	
	no(has_enemy(L4)),
	assume_enemy(L4),
	location_toward(L1,[0,-1],L5),
	no(has_enemy(L5)),	
	assume_enemy(L5),
	!.
add_enemy_KB(no) :-
	agent_location(L1),				
	location_toward(L1,[1,0],L2),	
	retractall(is_enemy(L2)),			
	location_toward(L1,[0,1],L3),	
	retractall(is_enemy(L3)),
	location_toward(L1,[-1,0],L4),	
	retractall(is_enemy(L4)),
	location_toward(L1,[0,-1],L5),	
	retractall(is_enemy(L5)),
	!.

		
assume_enemy(L) :- 		
	no(wall_location(L)),			% enemy can't be in a wall		
	no(is_visited(L)),
	no(is_saferoom(L)),
	retractall(is_enemy(L)),
	assert(is_enemy(L)),
	!.
assume_enemy(L).
	
	
% update our knowledge about pit presence

add_pit_KB(yes):-
	bleezy,	
	agent_location(L1),				% I don't know if I'm in a pit place.
	location_toward(L1,[1,0],L2),	% It's possible there are Pit in
	no(has_pit(L2)),
	assume_pit(L2),					% each adjacent room. <=|
	location_toward(L1,[0,1],L3),
	no(has_pit(L3)),
	assume_pit(L3),
	location_toward(L1,[-1,0],L4),	
	no(has_pit(L4)),
	assume_pit(L4),
	location_toward(L1,[0,-1],L5),	
	no(has_pit(L5)),
	assume_pit(L5),
	!.
	
add_pit_KB(no) :-
	agent_location(L1),				
	location_toward(L1,[1,0],L2),	
	retractall(is_pit(L2)),			
	location_toward(L1,[0,1],L3),	
	retractall(is_pit(L3)),
	location_toward(L1,[-1,0],L4),	
	retractall(is_pit(L4)),
	location_toward(L1,[0,-1],L5),	
	retractall(is_pit(L5)),
	!.
	
assume_pit(L) :- 
	no(wall_location(L)),			% No Pit in a wall...
	no(is_visited(L)),
	no(is_saferoom(L)),
	retractall(is_pit(L)),
	assert(is_pit(L)),
	!.
assume_pit(L).
	
			
	
% update our knowledge about teletransport
	
add_teletransport_KB(yes):-
	flash,	
	agent_location(L1),				% I don't know if I'm in a pit place.
	location_toward(L1,[1,0],L2),	% It's possible there are Pit in
	no(has_teletransport(L2)),
	assume_teletransport(L2),					% each adjacent room. <=|
	location_toward(L1,[0,1],L3),
	no(has_teletransport(L3)),	
	assume_teletransport(L3),
	location_toward(L1,[-1,0],L4),	
	no(has_teletransport(L4)),
	assume_teletransport(L4),
	location_toward(L1,[0,-1],L5),
	no(has_teletransport(L5)),
	assume_teletransport(L5),
	!.
	
add_teletransport_KB(no) :-
	agent_location(L1),				
	location_toward(L1,[1,0],L2),	
	retractall(is_teletransport(L2)),			
	location_toward(L1,[0,1],L3),	
	retractall(is_teletransport(L3)),
	location_toward(L1,[-1,0],L4),	
	retractall(is_teletransport(L4)),
	location_toward(L1,[0,-1],L5),	
	retractall(is_teletransport(L5)),
	!.


assume_teletransport(L) :- 
	no(wall_location(L)),			% No Pit in a wall...
	no(is_visited(L)),
	no(is_saferoom(L)),
	retractall(is_teletransport(L)),
	assert(is_teletransport(L)),
	!.
assume_teletransport(L).



add_saferoom_KB(no,no,no):-
	agent_location(L1),				
	location_toward(L1,[1,0],L2),		% And I'm sure there are no pit in
	assume_saferoom(L2), 				% each adjacent room. >=P
	location_toward(L1,[0,1],L3),	
	assume_saferoom(L3),
	location_toward(L1,[-1,0],L4),	
	assume_saferoom(L4),
	location_toward(L1,[0,-1],L5),	
	assume_saferoom(L5),
	!.		
add_saferoom_KB(Stench,Bleeze,Flash).
	
assume_saferoom(L) :-
	no(wall_location(L)),		
	no(is_visited(L)),
	no(has_enemy(L)),
	no(has_pit(L)),
	no(has_teletransport(L)),
	retractall(is_teletransport(L)),
	retractall(is_pit(L)),
	retractall(is_enemy(L)),
	retractall(is_saferoom(L)),
	assert(is_saferoom(L)),
	!.
assume_saferoom(L).	



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

add_certain_enemy_KB :-
	forall(is_enemy(L),assume_certain_enemy(L)),
	!.
add_certain_enemy_KB.


assume_certain_enemy(L):-
	no(has_enemy(L)),
	enemy_location(_,_,L),
	diagonal(L,BL,BR,TL,TR),
	saferoom_or_visited(BL,BR,TL,TR),
	certain_saferoom(BL),
	certain_saferoom(BR),
	certain_saferoom(TL),
	certain_saferoom(TR),
	retractall(is_enemy(BL)),
	retractall(is_enemy(BR)),
	retractall(is_enemy(TL)),
	retractall(is_enemy(TR)),
	retractall(is_enemy(L)),
	retractall(is_pit(L)),
	retractall(is_teletransport(L)),
	retractall(is_saferoom(L)),
	retractall(has_enemy(L)),
	assert(has_enemy(L)),
	!.
assume_certain_enemy(L):-
	no(has_pit(L)),
	diagonal(L,BL,BR,TL,TR),
	saferoom_or_visited(BL,BR,TL,TR),
	retractall(is_enemy(L)),
	retractall(is_saferoom(L)),
	assert(is_saferoom(L)),
	!.
assume_certain_enemy(L).


add_certain_pit_KB :-
	forall(is_pit(L),assume_certain_pit(L)),
	!.
add_certain_pit_KB.


assume_certain_pit(L):-
	no(has_pit(L)),
	pit_location(L),
	diagonal(L,BL,BR,TL,TR),
	saferoom_or_visited(BL,BR,TL,TR),
	certain_saferoom(BL),
	certain_saferoom(BR),
	certain_saferoom(TL),
	certain_saferoom(TR),
	retractall(is_pit(BL)),
	retractall(is_pit(BR)),
	retractall(is_pit(TL)),
	retractall(is_pit(TR)),
	retractall(is_enemy(L)),
	retractall(is_pit(L)),
	retractall(is_teletransport(L)),
	retractall(is_saferoom(L)),
	retractall(has_pit(L)),
	assert(has_pit(L)),
	!.
assume_certain_pit(L):-
	no(has_pit(L)),
	diagonal(L,BL,BR,TL,TR),
	saferoom_or_visited(BL,BR,TL,TR),
	retractall(is_pit(L)),
	retractall(is_saferoom(L)),
	assert(is_saferoom(L)),
	!.
assume_certain_pit(L).


add_certain_teletransport_KB :-
	forall(is_teletransport(L),assume_certain_teletransport(L)),
	!.
add_certain_teletransport_KB.



assume_certain_teletransport(L):-
	no(has_teletransport(L)),
	teletransport_location(L),
	diagonal(L,BL,BR,TL,TR),
	saferoom_or_visited(BL,BR,TL,TR),
	certain_saferoom(BL),
	certain_saferoom(BR),
	certain_saferoom(TL),
	certain_saferoom(TR),
	retractall(is_teletransport(BL)),
	retractall(is_teletransport(BR)),
	retractall(is_teletransport(TL)),
	retractall(is_teletransport(TR)),
	retractall(is_enemy(L)),
	retractall(is_pit(L)),
	retractall(is_teletransport(L)),
	retractall(is_saferoom(L)),
	retractall(has_teletransport(L)),
	assert(has_teletransport(L)),
	!.
assume_certain_teletransport(L):-
	no(has_pit(L)),
	diagonal(L,BL,BR,TL,TR),
	saferoom_or_visited(BL,BR,TL,TR),
	retractall(is_teletransport(L)),
	retractall(is_saferoom(L)),
	assert(is_saferoom(L)),
	!.
assume_certain_enemy(L).


certain_saferoom(D):-
	no(wall_location(D)),		
	no(is_visited(D)),
	is_enemy(D),
	no(is_pit(D)),
	no(is_teletransport(D)),
	no(enemy_location(_,_,D)),
	no(pit_location(D)),
	no(teletransport_location(D)),
	retractall(is_enemy(D)),
	retractall(is_saferoom(D)),
	assert(is_saferoom(D)),
	!.
certain_saferoom(D):-
	no(wall_location(D)),		
	no(is_visited(D)),
	is_pit(D),
	no(is_enemy(D)),
	no(is_teletransport(D)),
	no(enemy_location(_,_,D)),
	no(pit_location(D)),
	no(teletransport_location(D)),
	retractall(is_pit(D)),
	retractall(is_saferoom(D)),
	assert(is_saferoom(D)),
	!.
certain_saferoom(D):-
	no(wall_location(D)),		
	no(is_visited(D)),
	no(is_enemy(D)),
	no(is_pit(D)),
	is_teletransport(D),
	no(enemy_location(_,_,D)),
	no(pit_location(D)),
	no(teletransport_location(D)),
	retractall(is_teletransport(D)),
	retractall(is_saferoom(D)),
	assert(is_saferoom(D)),
	!.
certain_saferoom(D).


saferoom_or_visited(BL,BR,TL,TR):-
	(is_saferoom(BL);is_visited(BL)),(is_saferoom(TL);is_visited(TL)),BL = [X,Y],YF is (Y + 1), is_visited([X,YF]), !.
saferoom_or_visited(BL,BR,TL,TR):-
	(is_saferoom(TL);is_visited(TL)),(is_saferoom(TR);is_visited(TR)),TL = [X,Y],XF is (X + 1), is_visited([XF,Y]), !.
saferoom_or_visited(BL,BR,TL,TR):-
	(is_saferoom(TR);is_visited(TR)),(is_saferoom(BR);is_visited(BR)),TR = [X,Y],YF is (Y - 1), is_visited([X,YF]), !.
saferoom_or_visited(BL,BR,TL,TR):-
	(is_saferoom(BR);is_visited(BR)),(is_saferoom(BL);is_visited(BL)),BR = [X,Y],XF is (X - 1), is_visited([XF,Y]), !.
	
	
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	

% update our knowledge about gold presence

add_gold_KB(yes) :-
	glittering,
	agent_location(L),
	retractall(is_gold(L)),
	assert(is_gold(L)),
	!.		
add_gold_KB(no).
	
% update our knowledge about wall presence

add_wall_KB(yes) :-	
	bumped,		% here I know where there is wall
	agent_location(L),		% because I'm in ...
	retractall(is_wall(L)),
	assert(is_wall(L)),
	!.					
add_wall_KB(no).

% update our knowledge about powerup

add_powerup_KB(yes) :-
	powerup,
	agent_location(L),
	retractall(is_powerup(L)),
	assert(is_powerup(L)),
	!.		
add_powerup_KB(no).



		
