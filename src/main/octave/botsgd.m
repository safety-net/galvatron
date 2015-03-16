% Ensure training data is in same directory when running Octave script
x = load('trainingXVals.dat');
y = load('trainingYVals.dat');

m = length(y);

% Add intercept term to x
x = [ones(m,1),x];

theta_normal = (x' * x)\x' * y
