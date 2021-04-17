import SwiftUI
import shared

class HomeViewModel : ObservableObject, Identifiable {

    @State private var engine: HomeEngine = HomeEngine(coroutineScope: MainScope(), environment: Environment(database: DatabaseFactory().createDB()),
                                                                userSettings: UserSettingsStore())
    @Published var viewState: [HomeViewState] = []
    @Published var viewEvent: [HomeViewEvent] = []

    init() {
        viewState.removeAll()
        viewEvent.removeAll()
        viewState.append(engine.viewState.value as! HomeViewState)
        viewEvent.append(engine.viewState.value as! HomeViewEvent)
    }
}

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    @State private var gameName: String = ""
    @State private var isEditing = false
    @State private var selection: String? = nil
    @State private var viewModel:  HomeViewModel = HomeViewModel()

    init() {
//         viewModel.launch()
        for entity in viewModel.viewState.first!.entities {
            let unwrappedEntity = entity as? GameRowEntity.HeaderType
            let title = unwrappedEntity?.title ?? ""
            print( "Hello " + title)
        }
    }


    var body: some View {
        NavigationView {
//             VStack(spacing: 0) {
//                 ForEach(viewModel.viewState.entities) { entity in
//                     if entity is GameRowEntity.HeaderType {
//                         Text((entity as GameRowEntity.HeaderType).title)
//                     } else {
//                         Text((entity as GameRowEntity.BodyType).game.name)
//                     }
//                 }
//             }
            VStack(spacing: 0) {
                Text("Create a new game").multilineTextAlignment(.center)
                TextField(
                        "Enter a new game",
                         text: $gameName
                    ) { isEditing in
                        self.isEditing = isEditing
                    } onCommit: {
//                         validate(name: gameName)
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
