package com.division.fearforall.engines;

import com.division.fearforall.core.FearForAll;
import java.util.*;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Evan
 */
public class EngineManager {

    private Map<String, Engine> activeEngines = new HashMap<String, Engine>();
    private List<Engine> engineQueue = new ArrayList<Engine>();

    public void EngineManager() {
    }

    public void registerEngine(Engine engine) throws EngineException {
        final EngineInfo engDep = engine.getClass().getAnnotation(EngineInfo.class);
        if (engDep != null) {
            String[] depends = engDep.depends();
            for (String s : depends) {
                if (!isRegistered(s)) {
                    if (!engineQueue.contains(engine)) {
                        engineQueue.add(engine);
                    }
                    return;
                }
            }
        } else {
            throw new EngineException("Unable to find Engine Info.");
        }
        try {
            engine.runStartupChecks();
            if (!activeEngines.containsKey(engine.getName())) {
                activeEngines.put(engine.getName(), engine);
                System.out.println("[FearForAll] Engine: " + engine.getName() + " has been started.");
                if (engineQueue.contains(engine)) {
                    engineQueue.remove(engine);
                }
                FearForAll.getInstance().getServer().getPluginManager().registerEvents(engine, FearForAll.getInstance());
                runEngineQueue();
            } else {
                throw new EngineException("[FearForAll] Engine with name: " + engine.getName() + " already is registered.");
            }
        } catch (EngineException ex) {
            System.out.println("[FearForAll] Unabled to start Engine: " + engine.getName() + " Reason: " + ex.getMessage());
        }
    }

    public Engine getEngine(String engineName) {
        if (activeEngines.containsKey(engineName)) {
            return activeEngines.get(engineName);
        }
        return null;
    }

    public void unregisterEngine(String engineName) {
        Engine eng = activeEngines.get(engineName);
        HandlerList.unregisterAll(eng);
        System.out.println("[FearForall] Engine: " + eng.getName() + " has been stopped.");
        activeEngines.remove(engineName);
    }

    public boolean isRegistered(String engineName) {
        return activeEngines.containsKey(engineName);
    }

    public Set<String> getEngines() {
        return activeEngines.keySet();
    }

    public String getEngineVersion(String engineName) {
        Engine eng = activeEngines.get(engineName);
        final EngineInfo engDep = eng.getClass().getAnnotation(EngineInfo.class);
        return engDep.version();
    }

    public String getEngineAuthor(String engineName) {
        Engine eng = activeEngines.get(engineName);
        final EngineInfo engDep = eng.getClass().getAnnotation(EngineInfo.class);
        return engDep.author();
    }

    public void unregisterAllEngines() {
        Set<String> engines = activeEngines.keySet();
        String[] tempStorage = new String[engines.size()];
        engines.toArray(tempStorage);
        for (String s : tempStorage) {
            unregisterEngine(s);
        }
    }

    public void runEngineQueue() {
        Engine[] tempStorage = new Engine[engineQueue.size()];
        engineQueue.toArray(tempStorage);
        for (Engine eng : tempStorage) {
            try {
                registerEngine(eng);
            } catch (EngineException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
