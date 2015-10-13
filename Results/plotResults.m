close all;

output1 = csvread('Output');

figure(1);
plot(output1(:,1), output1(:,2));
xlabel('Runs');
ylabel('Number of epochs per run');
saveas(gcf, 'Epochs', 'png');

figure(2);
plot(output1(:,1), output1(:,3));
xlabel('Runs');
ylabel('Global success');
saveas(gcf, 'Success', 'png');

figure(3);
hold on;
plot(output1(:,1), output1(:,4), 'black');
plot(output1(:,1), output1(:,5), 'magenta');
plot(output1(:,1), output1(:,6), 'blue');
plot(output1(:,1), output1(:,7), 'red');
plot(output1(:,1), output1(:,8), 'black--');
plot(output1(:,1), output1(:,9), 'm--');
plot(output1(:,1), output1(:,10), 'b--');
plot(output1(:,1), output1(:,11), 'r--');
ylim([0 1]);
xlabel('Runs');
ylabel('Utility');
legend('Majority cops/majority civilians', 'Majority cops/minority civilians', 'Minority cops/majority civilians', 'Minority cops/minority civilians');
saveas(gcf, 'Utility', 'png');

meanEpochs = mean(output1(:,2));
sdEpochs = std(output1(:,2));
meanSuccess = mean(output1(:,3));
sdSuccess = std(output1(:,3));


