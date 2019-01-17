/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

/**
 *
 * @author eleve
 */
public class Temps {
    
    class StopWatch {
 
        
        long startTime;
        long endTime;

        //Constructors
        public StopWatch () { 
         }
 
        //Methods
        long start() {
            long startTime = System.currentTimeMillis();
            return startTime; 
        }

        long stop() {
            long endTime = System.currentTimeMillis();
            return endTime; 
        }

        long getElapsedTime() {
            return (endTime - startTime);
        }
    }
}
