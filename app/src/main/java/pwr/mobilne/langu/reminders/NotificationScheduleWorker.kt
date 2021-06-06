package pwr.mobilne.langu.reminders

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import pwr.mobilne.langu.activities.MainActivity
import pwr.mobilne.langu.data.WordDatabase
import kotlin.random.Random

class NotificationScheduleWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val word = WordDatabase.getDatabase(context).userDao().getRandomWord(MainActivity.appLanguage)
        val text = "Your word for today is '" + word.german + "' which means '" + word.nativs + "'"
        val builder = NotificationCompat.Builder(context, 0.toString())
            .setSmallIcon(
                context.resources.getIdentifier(
                    "langu_logo",
                    "drawable",
                    context.applicationInfo.packageName
                )
            )
            .setContentTitle("Word for today")
            .setContentText(text)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text + "\nLanguage: " + word.laguage.displayName)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())
        }

        return Result.success()
    }

}