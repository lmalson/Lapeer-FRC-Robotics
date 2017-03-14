package org.lapeerrobotics.frc.team5460;

public class MedianFilter {

	double aa;
	double bb;
	double cc;
	double dd;
	boolean fInitialized;
	
	public MedianFilter() {
		fInitialized = false;
	}
	
	public double filter(double ee) {
		
		double median = 0.0;		
		double a = aa;
		double b = bb;
		double c = cc;
		double d = dd;
		double e = ee;
		
		if (!fInitialized)
		    {
			    a = b = c = d = e;
		        fInitialized = true;
		    }
		
		double median2 = 0;

		if (a < b) { // 1			
			if (c < d) { // 1.1
				if (a < c) { // 1.1.1 - eliminate a (b|cd|e)
					if (b < e) { // 1.1.1.1 (cd|be)
						if (c < b) { // 1.1.1.1.1 - eliminate c (d|be)
							if (d < b) { // (dbe)
								median2 = d;
							}
							else { // (b|de)
								median2 = b;
							}
						}
						else { // 1.1.1.1.2 - eliminate b (cd|e)
							if (e < c) { // (ecd)
								median2 = e;
							}
							else { // (c|de)
								median2 = c;
							}							
						}
					} else { // 1.1.1.2 (cd|eb)
						if (c < e) { // 1.1.1.2.1 - eliminate c (d|eb)
							if (d < e) { // (deb)
								median2 = d;
							}
							else { // (e|bd)
								median2 = e;
							}
						}
						else { // 1.1.1.2.2 - eliminate e (cd|b)
							if (b < c) { // (bcd)
								median2 = b;
							}
							else { // (c|db)
								median2 = c;
							}							
						}						
					}					
				}
				else { // 1.1.2 - eliminate c (ab|d|e)
					if (d < e) { // 1.1.2.1 (ab|de)
						if (a < d) { // 1.1.2.1.1 - eliminate a (b|de)
							if (b < d) { // (bde)
								median2 = b;
							}
							else { // (d|be)
								median2 = d;
							}
						}
						else { // 1.1.2.1.2 - eliminate d (ab|e)
							if (e < a) { // (eab)
								median2 = e;
							}
							else { // (a|be)
								median2 = a;
							}							
						}												
					} else { // 1.1.2.2 (ab|ed)
						if (a < e) { // 1.1.2.2.1 - eliminate a (b|ed)
							if (b < e) { // (bed)
								median2 = b;
							}
							else { // (e|bd)
								median2 = e;
							}
						}
						else { // 1.1.2.2.2 - eliminate e (ab|d)
							if (d < a) { // (dab)
								median2 = d;
							}
							else { // (a|bd)
								median2 = a;
							}							
						}												
					}										
				}					
			}
			else { // 1.2
				if (a < d) { // 1.2.1 - eliminate a (b|dc|e)
					if (b < e) { // 1.2.1.1 (be|dc)
						if (b < d) { // 1.2.1.1.1 - eliminate b (e|dc)
							if (e < d) { // (edc)
								median2 = e;
							}
							else { // (d|ec)
								median2 = d;
							}
						}
						else { // 1.2.1.1.2 - eliminate d (be|c)
							if (c < b) { // (cbe)
								median2 = c;
							}
							else { // (b|ec)
								median2 = b;
							}							
						}																		
					} else { // 1.2.1.2 (eb|dc)
						if (e < d) { // 1.2.1.2.1 - eliminate e (b|dc)
							if (b < d) { // (bdc)
								median2 = b;
							}
							else { // (d|bc)
								median2 = d;
							}
						}
						else { // 1.2.1.2.2 - eliminate d (eb|c)
							if (c < e) { // (ceb)
								median2 = c;
							}
							else { // (e|bc)
								median2 = e;
							}							
						}																								
					}
				}
				else { // 1.2.2 - eliminate d (ab|c|e)
					if (c < e) { // 1.2.2.1 (ab|ce)
						if (a < c) { // 1.2.2.1.1 - eliminate a (b|ce)
							if (b < c) { // (bce)
								median2 = b;
							}
							else { // (c|be)
								median2 = c;
							}
						}
						else { // 1.2.2.1.2 - eliminate c (ab|e)
							if (e < a) { // (eab)
								median2 = e;
							}
							else { // (a|be)
								median2 = a;
							}							
						}																														
					} else { // 1.2.2.2 (ab|ec)
						if (a < e) { // 1.2.2.2.1 - eliminate a (b|ec)
							if (b < e) { // (bec)
								median2 = b;
							}
							else { // (e|bc)
								median2 = e;
							}
						}
						else { // 1.2.2.2.2 - eliminate e (ab|c)
							if (c < a) { // (cab)
								median2 = c;
							}
							else { // (a|bc)
								median2 = a;
							}							
						}																														
					}					
				}				
			}
		} 
		else { // 2
			if (c < d) { // 2.1
				if (b < c) { // 2.1.1 - eliminate b (a|cd|e)
					if (a < e) { // 2.1.1.1 (cd|ae)
						if (a < c) { // 2.1.1.1.1 - eliminate a (cd|e)
							if (e < c) { // (ecd)
								median2 = e;
							}
							else { // (c|de)
								median2 = c;
							}
						}
						else { // 2.1.1.1.2 - eliminate c (d|ae)
							if (d < a) { // (dae)
								median2 = d;
							}
							else { // (a|de)
								median2 = a;
							}							
						}																																				
					} else { // 2.1.1.2 (cd|ea)
						if (e < c) { // 2.1.1.2.1 - eliminate e (cd|a)
							if (a < c) { // (acd)
								median2 = a;
							}
							else { // (c|da)
								median2 = c;
							}
						}
						else { // 2.1.1.2.2 - eliminate c (d|ea)
							if (d < e) { // (dea)
								median2 = d;
							}
							else { // (e|da)
								median2 = e;
							}							
						}																																										
					}										
				}
				else { // 2.1.2 - eliminate c (ba|d|e)
					if (d < e) { // 2.1.2.1 (ba|de)
						if (d < b) { // 2.1.2.1.1 - eliminate d (ba|e)
							if (e < b) { // (eba)
								median2 = e;
							}
							else { // (b|ae)
								median2 = b;
							}
						}
						else { // 2.1.2.1.2 - eliminate b (a|de)
							if (a < d) { // (ade)
								median2 = a;
							}
							else { // (d|ae)
								median2 = d;
							}							
						}																																																
					} else { // 2.1.2.2 (ba|ed)
						if (e < b) { // 2.1.2.2.1 - eliminate e (ba|d)
							if (d < b) { // (dba)
								median2 = d;
							}
							else { // (b|ad)
								median2 = b;
							}
						}
						else { // 2.1.2.2.2 - eliminate b (a|ed)
							if (a < e) { // (aed)
								median2 = a;
							}
							else { // (e|ad)
								median2 = e;
							}							
						}																																																						
					}															
				}					
			}
			else { // 2.2
				if (b < d) { // 2.2.1 - eliminate b (a|dc|e)
					if (a < e) { // 2.2.1.1 (dc|ae)
						if (a < d) { // 2.2.1.1.1 - eliminate a (dc|e)
							if (e < d) { // (edc)
								median2 = e;
							}
							else { // (d|ce)
								median2 = d;
							}
						}
						else { // 2.2.1.1.2 - eliminate d (c|ae)
							if (c < a) { // (cae)
								median2 = c;
							}
							else { // (a|ce)
								median2 = a;
							}							
						}																																																												
					} else { // 2.2.1.2 (dc|ea)
						if (e < d) { // 2.2.1.2.1 - eliminate e (dc|a)
							if (a < d) { // (adc)
								median2 = a;
							}
							else { // (d|ca)
								median2 = d;
							}
						}
						else { // 2.2.1.2.2 - eliminate d (c|ea)
							if (c < e) { // (cea)
								median2 = c;
							}
							else { // (e|ca)
								median2 = e;
							}							
						}																																																																		
					}																				
				}
				else { // 2.2.2 - eliminate d (ba|c|e)
					if (c < e) { // 2.2.2.1 (ba|ce)
						if (c < b) { // 2.2.2.1.1 - eliminate c (ba|e)
							if (e < b) { // (eba)
								median2 = e;
							}
							else { // (b|ae)
								median2 = b;
							}
						}
						else { // 2.2.2.1.2 - eliminate b (a|ce)
							if (a < c) { // (ace)
								median2 = a;
							}
							else { // (c|ae)
								median2 = c;
							}							
						}																																																																								
					} else { // 2.2.2.2 (ba|ec)
						if (e < b) { // 2.2.2.2.1 - eliminate e (ba|c)
							if (c < b) { // (cba)
								median2 = c;
							}
							else { // (b|ac
								median2 = b;
							}
						}
						else { // 2.2.2.2.2 - eliminate b (a|ec)
							if (a < e) { // (aec)
								median2 = a;
							}
							else { // (e|ac)
								median2 = e;
							}							
						}																																																																														
					}																									
				}				
			}
		}
		
	median = b < a ? d < c ? b < d ? a < e ? a < d ? e < d ? e : d
				                   : c < a ? c : a
				           : e < d ? a < d ? a : d
				                   : c < e ? c : e
				   : c < e ? b < c ? a < c ? a : c
				                   : e < b ? e : b
				           : b < e ? a < e ? a : e
				                   : c < b ? c : b
				: b < c ? a < e ? a < c ? e < c ? e : c
				                   : d < a ? d : a
				           : e < c ? a < c ? a : c
				                   : d < e ? d : e
				   : d < e ? b < d ? a < d ? a : d
				                   : e < b ? e : b
				           : b < e ? a < e ? a : e
				                   : d < b ? d : b
				: d < c ? a < d ? b < e ? b < d ? e < d ? e : d
				                   : c < b ? c : b
				           : e < d ? b < d ? b : d
				                   : c < e ? c : e
				   : c < e ? a < c ? b < c ? b : c
				                   : e < a ? e : a
				           : a < e ? b < e ? b : e
				                   : c < a ? c : a
				: a < c ? b < e ? b < c ? e < c ? e : c
				                   : d < b ? d : b
				           : e < c ? b < c ? b : c
				                   : d < e ? d : e
				   : d < e ? a < d ? b < d ? b : d
				                   : e < a ? e : a
				           : a < e ? b < e ? b : e
				                   : d < a ? d : a;		

//	if (median2 != median)
//		System.out.println("\n\nERROR Not Equal, median2: "+median2+", Median of "+aa+","+bb+","+cc+","+dd+","+ee+" median: "+median);
	
		aa = bb;
		bb = cc;
		cc = dd;
		dd = ee;

		return median2;		
	
//		if (flag)
//		else
//			return median2;
	}

}
