package concepts;

import ilog.concert.IloException;

public interface TaskScheduler {
    public void solve() throws IloException;
}
