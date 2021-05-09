import SwiftUI
import Combine
import shared

func greet() -> String {
    return Greeting().greeting()
}

class HomeViewModel: ObservableObject {

    private let repository = AppRepository(database: DatabaseFactory().createDB())
      @Published var games = [Game]()

    func addGame() {
         repository.storeDummyGame(completionHandler :{ gamesData, error in
       })
    }

    func fetch() {
        addGame()
        games.removeAll()
        repository.loadGames(completionHandler: { gamesData, error in
                guard let gamesData = gamesData else { return }
                var games = gamesData as! [Game]
                self.games += games
                print( "size of values is : \(games.count)" )
        })
    }
}

struct ContentView: View {
    @State private var gameName: String = ""
    @State private var isEditing = false
    @State private var selection: String? = nil

    @State var viewModel = HomeViewModel()

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                ForEach(viewModel.games, id: \.id) { game in
                    Text(game.name)
                }
            }
            VStack(spacing: 0) {
                Text("Create a new game").multilineTextAlignment(.center)
                TextField(
                        "Enter a new game",
                         text: $gameName
                    ) { isEditing in
                        self.isEditing = isEditing
                    } onCommit: {
                        viewModel.addGame()
                    }
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                    .border(Color(UIColor.separator)).padding()
                NavigationLink(destination: Text("Second View"), tag: "Second", selection: $selection) { EmptyView() }
                NavigationLink(destination: Text("Third View"), tag: "Third", selection: $selection) { EmptyView() }


                Button("Start New Game") {
                    self.selection = "Second"
                }
            }
            .navigationBarTitle("Score Book")
            Spacer()
        }.padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
