/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.engine.sprite.base.SimpleVector3;

/**
 * @author Donald
 */
public class DamageInfo {
	boolean			active;
	long			area;					// damage area type
	float			damages;
	long			duration;				// in milliseconds
	short[]			except	= new short[10];
	boolean			exist;
	long			flags;					// damages flags
	// any other customization
	long			lastupd;
	SimpleVector3	pos;
	float			radius;
	// -1 for apply once
	// else damage *=framediff
	long			source;					// io index or -1 for player
	long			special;				// slowdown, paralysis...
	long			special_ID;				// for io localised immunities or
	long			start_time;
	long			type;					// damages type
}
